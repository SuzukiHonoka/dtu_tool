// low_initDlg.cpp : implementation file
//

#include "stdafx.h"
#include "formatkey.h"
#include "formatkeyDlg.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CFormatKeyDlg dialog

CFormatKeyDlg::CFormatKeyDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CFormatKeyDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CFormatKeyDlg)
	m_strSoPin = _T("");
	m_strTokenName = _T("");
	m_strUserPin = _T("");
	m_strResult = _T("");
	m_strTotalResult = _T("");
	m_dwPrvSize = 0;
	m_dwPubSize = 0;
	m_dwRsaCount = 0;
	m_ucSoPinEc = 0;
	m_ucUserPinEc = 0;
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);

	m_dwSucceededCount = 0;
	m_dwFailedCount = 0;
}

void CFormatKeyDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CFormatKeyDlg)
	DDX_Control(pDX, IDOK, m_btnOk);
	DDX_Text(pDX, IDC_EDIT_SOPIN, m_strSoPin);
	DDX_Text(pDX, IDC_EDIT_TOKEN_NAME, m_strTokenName);
	DDX_Text(pDX, IDC_EDIT_USERPIN, m_strUserPin);
	DDX_Text(pDX, IDC_MSG_RESULT, m_strResult);
	DDX_Text(pDX, IDC_MSG_TOTALRESULT, m_strTotalResult);
	DDX_Text(pDX, IDC_EDIT_PRVSIZE, m_dwPrvSize);
	DDX_Text(pDX, IDC_EDIT_PUBSIZE, m_dwPubSize);
	DDX_Text(pDX, IDC_EDIT_RSA_COUNT, m_dwRsaCount);
	DDV_MinMaxDWord(pDX, m_dwRsaCount, 1, 9);
	DDX_Text(pDX, IDC_EDIT_SOPIN_EC, m_ucSoPinEc);
	DDV_MinMaxByte(pDX, m_ucSoPinEc, 0, 15);
	DDX_Text(pDX, IDC_EDIT_USERPIN_EC, m_ucUserPinEc);
	DDV_MinMaxByte(pDX, m_ucUserPinEc, 0, 15);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CFormatKeyDlg, CDialog)
	//{{AFX_MSG_MAP(CFormatKeyDlg)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CFormatKeyDlg message handlers

BOOL CFormatKeyDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	m_strTokenName = "ePass3003";
	m_strSoPin = "rockey";
	m_strUserPin = "1234";
	m_ucSoPinEc = 6;
	m_ucUserPinEc = 6;
	m_dwPubSize = 16000;
	m_dwPrvSize = 15000;
	m_dwRsaCount = 9;
	m_strResult = "Not initialize any usb token";
	m_strTotalResult = "Total: 0, succeeded: 0, failed: 0.";

	UpdateData(FALSE);

	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CFormatKeyDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CFormatKeyDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}


void CFormatKeyDlg::OnOK() 
{
	UpdateData(TRUE);

	AUX_INIT_TOKEN_LOWLEVL_PKI param = {0};
	param.version.major = 0x01;
	param.version.minor = 0x00;
	param.strTokenName = m_strTokenName.GetBuffer(m_strTokenName.GetLength());
	param.strSOPin = m_strSoPin.GetBuffer(m_strSoPin.GetLength());
	param.strUserPin = m_strUserPin.GetBuffer(m_strUserPin.GetLength());
	param.ucSOMaxPinEC = m_ucSoPinEc;
	param.ucUserMaxPinEC = m_ucUserPinEc;
	param.nRSAKeyPairCount = (BYTE)m_dwRsaCount;
	param.ulPubSize = m_dwPubSize;
	param.ulPrvSize = m_dwPrvSize;
	param.nDSAKeyPairCount = 9;

	m_btnOk.EnableWindow(FALSE);

	AUX_FUNC_LIST_PTR pAuxFunc = NULL;
	CK_RV rv = CKR_OK;
	CK_SLOT_ID slotList[10] = {0};
	CK_ULONG ulList = 10;
	
	if (IDYES != MessageBox("Format will destroy all data in your USBKey, are you sure?", "Warning", MB_YESNO | MB_ICONWARNING))
	{
		goto LABEL_END;
	}

	rv = C_GetSlotList(CK_TRUE, slotList, &ulList);
	if (CKR_OK != rv)
	{
		MessageBox("Can not get slot list!", "ERROR", MB_OK | MB_ICONERROR);
		goto LABEL_END;
	}
	if (0 == ulList)
	{
		MessageBox("USB token is not attached to your USB port!", "ERROR", MB_OK | MB_ICONERROR);
		goto LABEL_END;
	}
	if (1 < ulList)
	{
		MessageBox("More than one USB token has been attached to your USB port! Only one can be leaved!", "ERROR", MB_OK | MB_ICONERROR);
		goto LABEL_END;
	}

	rv = E_GetAuxFunctionList(&pAuxFunc);
	if (CKR_OK != rv || NULL == pAuxFunc)
	{
		MessageBox("Can not get the format function information!", "ERROR", MB_OK | MB_ICONERROR);
		goto LABEL_END;
	}

	{
		CWaitCursor waitcursor;
		rv = ((EP_InitTokenPrivate)(pAuxFunc->pFunc[EP_INIT_TOKEN_PRIVATE]))(slotList[0], &param);
		if (CKR_OK == rv)
		{
			++m_dwSucceededCount;
			m_strResult = "Initialize succeeded.";
		}
		else
		{
			++m_dwFailedCount;
			m_strResult = "Initialize failed!";
		}
	}

	m_strTotalResult.Format("Total: %d, succeeded: %d, failed: %d.", m_dwSucceededCount + m_dwFailedCount, m_dwSucceededCount, m_dwFailedCount);

LABEL_END:

	m_btnOk.EnableWindow(TRUE);

	m_strTokenName.ReleaseBuffer();
	m_strSoPin.ReleaseBuffer();
	m_strUserPin.ReleaseBuffer();

	UpdateData(FALSE);
}
