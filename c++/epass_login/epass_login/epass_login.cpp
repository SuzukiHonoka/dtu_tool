#include<iostream>
#include<windows.h>
//#include "stdafx.h"
#include <string>
#include <atlstr.h>

#include "cryptoki_ext.h"
#include "cryptoki_win32.h"

using namespace std;

int main() {

	cout << "Hello World!" << endl;
	printf("Hello World!\n");
	HINSTANCE hDllInst;
	hDllInst = LoadLibrary(L"ShuttleCsp11_3003.dll"); //调用DLL
	if (!hDllInst) {
		printf("hDllInst error \n");
		return -1;
	}
	/*
		typedef int(*PLUSFUNC)(int a, int b); //后边为参数，前面为返回值
		PLUSFUNC plus_str = (PLUSFUNC)GetProcAddress(hDllInst, "add"); //GetProcAddress为获取该函数的地址
		std::cout << plus_str(1, 2);
	*/
	typedef unsigned long int(*FUNC_PKS_SESSION)(
		unsigned long int            slotID,        /* the slot's ID */
		unsigned long int              flags,         /* from CK_SESSION_INFO */
		void *                pApplication,  /* passed to callback */
		void *                 Notify,        /* callback function */
		unsigned long int * phSession      /* gets session handle */
	); //后边为参数，前面为返回值
	FUNC_PKS_SESSION C_OpenSession = (FUNC_PKS_SESSION)GetProcAddress(hDllInst, "C_OpenSession"); //GetProcAddress为获取该函数的地址

	typedef unsigned long int(*FUNC_PKS_Login)(
		unsigned long int hSession,  /* the session's handle */
		unsigned long int      userType,  /* the user type */
		unsigned char *   pPin,      /* the user's PIN */
		unsigned long int          ulPinLen   /* the length of the PIN */
	); //后边为参数，前面为返回值
	FUNC_PKS_Login C_Login = (FUNC_PKS_Login)GetProcAddress(hDllInst, "C_Login"); //GetProcAddress为获取该函数的地址

	typedef unsigned long int(*FUNC_PKS_init)(
		void *pPin
	);
	FUNC_PKS_init C_Initialize = (FUNC_PKS_init)GetProcAddress(hDllInst, "C_Initialize"); //GetProcAddress为获取该函数的地址

	typedef unsigned long int(*FUNC_PKS_exit)(
		void *pPin
	);
	FUNC_PKS_exit C_Finalize = (FUNC_PKS_exit)GetProcAddress(hDllInst, "C_Finalize"); //GetProcAddress为获取该函数的地址

	unsigned long int rv;
	unsigned long int m_ulSlotId = 0;
	unsigned long int hSession = NULL_PTR;
	void * m_pApplication;
	m_pApplication = new char[255];
	ZeroMemory(m_pApplication, 255);
	memcpy((char*)m_pApplication, "ePass3003",strlen("ePass3003"));

	printf("m_pApplication %s \n ", m_pApplication);

	rv = C_Initialize(0);
	if(CKR_OK != rv)
	{
		printf("C_Initialize fail \n");
		return -1;
	}
	// C_Finalize(0);
	printf("C_Initialize OK \n");

	rv = C_OpenSession(m_ulSlotId, 0x00000002UL | 0x00000004UL,&m_pApplication, 0, &hSession);
	printf("C_OpenSession %x \n", rv);

	if (CKR_OK == rv)
	{
		char pszPinUtf8[100];
		printf("open sesion ok! \n");

		memset(pszPinUtf8, 0x00, 100);
		memcpy(pszPinUtf8,"1234",4);

		printf("pszPinUtf8 %s \n", pszPinUtf8);

		CK_RV rv = C_Login(hSession, CKU_USER, (unsigned char*)pszPinUtf8, 4);
		if (CKR_OK != rv)
		{
			printf("login error! \n");
			return -1;
		}
		else {
			printf("login of! \n");
			return 0;
		}

		return 0;
	}
	else {
		printf("open sesion fail! \n");
		return -1;
	}

	goto end;


end:

	C_Finalize(0);
	return 0;
}