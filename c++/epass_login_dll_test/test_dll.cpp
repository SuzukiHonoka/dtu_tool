#include<iostream>
#include<windows.h>
using namespace std;

int main() {

	char buf[100];
	int ret;
	///*
	HINSTANCE hDllInst;
	hDllInst = LoadLibrary(L"epass_login_dll.dll"); //调用DLL
	if (!hDllInst) {
		printf("hDllInst error \n");
		return -1;
	}
	
	typedef int(*FUNC_LOGIN)(
		char* buf,
		char len
		);

	FUNC_LOGIN login_test_func = (FUNC_LOGIN)GetProcAddress(hDllInst, "epass_login");

	/// <summary>
	/// 需要做检查
	/// </summary>
	/// <returns></returns>
	if (login_test_func == NULL) {
		printf("login_test_func is null \n");
		return -1;
	}

	memset(buf,0,100);
	memcpy(buf,"12345",strlen("12345"));

	ret = login_test_func(buf,strlen(buf));
	//*/

	printf("test end ret %d \n", ret);
	return -1;
}