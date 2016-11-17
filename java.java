# Bug.py
import urllib.request
import urllib
import re
import threading
import time

#---ģ�黯---#
class Spider_Model:
	
	def __init__(self):
		self.page = 1	#��ʼҳ��
		self.pages = []	#��Ŷ��ӵ�list
		self.enable = False
	def GetPage(self,page):	#����ҳ�������ض���list
		print('%s�߳����ڻ�ȡ��%sҳ...' % (threading.current_thread().name, page))
		myUrl = "http://www.qiushibaike.com/8hr/page/" + page
		user_agent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2504.0 Safari/537.36'
		headers = {'User-Agent': user_agent}
		req = urllib.request.Request(myUrl, headers=headers)
		myResponse = urllib.request.urlopen(req)
		myPage = myResponse.read()
		unicodePage = myPage.decode('utf-8', 'ignore')	#ignore����Ҫ
		myItems = re.findall(r'<div class="content">(.*?)<!--', unicodePage, re.S | re.M)
		items = []
		for item in myItems:
			items.append(item.replace("<br/>", "\n"))
		return items    #����Ϳ���˶��ӵ�list


	#���Ӽ�����
	#����Ч����self.pages=[[��nҳ����],[��n+1ҳ����]]
	#self.pages��Ԫ������2ʱ����GetPage��ȡn+2ҳ����list����slef.pages
	def LoadPage(self):
		while self.enable:  #�˳����
			if len(self.pages) < 2:	#��ǰ������С��2
				try:
					myPage = self.GetPage(str(self.page))
					self.page += 1  #��ȡ��ϣ�ҳ��+1
					self.pages.append(myPage)  #����ȡ�Ķ���list��ӵ�self.pages list��
				except Exception as e:
					print('except:', e)
					print('�޷��������°ٿƣ�\n')
			else:
					time.sleep(1)  #���ӳ��㣬��Ϣ��

	#������ʾ��
	def ShowPage(self, nowPage, page):
		for items in nowPage:	#��������list����print���û�input����
			print(u'\n ��%dҳ' % page, items)
			myInput = input()
			if myInput == 'quit':  #�˳���־��ͬʱ���������߳�
				self.enable = False
				break

	def Start(self):
		self.enable = True
		page = self.page
		t = threading.Thread(target=self.LoadPage, name='SON')
		t.start()  #���߳�  #ֻ������threading����
		#_thread.start_new_thread(self.LoadPage,())

		while self.enable:
			if self.pages:  #�˳����
				nowPage = self.pages[0]  #ȡ��һҳlist��nowPage
				del self.pages[0]  
				self.ShowPage(nowPage, page)  #����һҳlist��ҳ�뷢����ʾ��
				page += 1  #��ҳ������һ��������ѭ��

#----------- �������ڴ� -----------
print(u"""
---------------------------------------
   ԭ�ĵ�ַ(Դ����):http://blog.csdn.net/pleasecallmewhy/article/details/8932310
   �����ܰ�����
   �汾��0.3
   ���ߣ�why
   ���ڣ�2014-06-03
   ���ԣ�Python 2.7
   ����������quit�˳��Ķ����°ٿ�
   ���ܣ����»س�����������յ��ܰ��ȵ�
---------------------------------------
   Դ��ַ��https://github.com/YeXiaoRain/BoringCode/blob/master/qiubaipachong.py
   �޸�����:��ַ��������ʽ����
   �޸���:Ryan
   �޸�����:2015-09-19
   ����ͨ���汾:Python 2.7.10
---------------------------------------
   �޸�����: python3, ����������ע��
   �޸���:Errorld
   �޸�����:2015-09-25
   ����ͨ���汾:Python 3.4.3
---------------------------------------
""")
myModel = Spider_Model()				
try:
	myModel.page = int(input('ѡ����ʼҳ��\n'))
except Exception as e:
	print(e)
	print('�������󣬴ӵ�һҳ��ʼ\n')
myModel.Start()
