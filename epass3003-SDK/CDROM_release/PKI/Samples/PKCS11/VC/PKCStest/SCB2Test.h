/********************************************************************
	Filename: 	destest.h
	Author:		Yubo
	
	Purpose:	interface for the Scb2Test class.
*********************************************************************/
#if !defined _SCB2TEST_H_
#define _SCB2TEST_H_

#include "BaseAll.h"

class Scb2Test : public CBaseAll  
{
public:
	Scb2Test(char* dll_file_path);
	virtual ~Scb2Test();
	void Test(void);
private:
	void GenerateKey(void);
	void crypt_Single(void);
	void crypt_Update(void);
	
private:
	CK_OBJECT_HANDLE m_hKey;
};

#endif // _SCB2TEST_H_
