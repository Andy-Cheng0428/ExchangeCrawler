from bs4 import BeautifulSoup
import bs4
import requests
import re
import json


def getHtmlText(url):
    try:
        response = requests.request('GET', url, timeout=30)
        response.raise_for_status()
        return response.text
    except:
        return '連線發生錯誤'


def analysizeHtml(htmlContent):
    bs = BeautifulSoup(htmlContent, 'html.parser')
    table = bs.find('table')  # 找到第一個表格
    tbody = table.find_next('tbody')  # 找到表格內容
    trs = tbody.find_all_next('tr')
    list = []
    for tr in trs:
        if isinstance(tr, bs4.element.Tag):
            tdCurr = tr.find('td', {'data-table': '幣別'})
            tdSpotSelling = tr.find('td', {'data-table': '本行即期買入'})
            tdSpotBuying = tr.find('td', {'data-table': '本行即期賣出'})
            tdCashSelling = tr.find('td', {'data-table': '本行現金賣出'})
            tdCashBuying = tr.find('td', {'data-table': '本行現金買入'})
            map = {}
            if isinstance(tdSpotSelling, bs4.element.Tag):
                map['spotSelling'] = tdSpotSelling.string
            if isinstance(tdSpotBuying, bs4.element.Tag):
                map['spotBuying'] = tdSpotBuying.string
            if isinstance(tdCashSelling, bs4.element.Tag):
                map['cashSelling'] = tdCashSelling.string
            if isinstance(tdCashBuying, bs4.element.Tag):
                map['cashBuying'] = tdCashBuying.string
            if isinstance(tdCurr, bs4.element.Tag):
                tdDivCurr = tdCurr.find('div', {'class': 'hidden-phone print_show'})
                if isinstance(tdDivCurr, bs4.element.Tag):
                    s = re.search('[A-Za-z]+', tdDivCurr.string)
                    if isinstance(s, re.Match):
                        map['curr'] = s.group(0)
            list.append(map)
    return list


if __name__ == '__main__':
    print("Taiwan")
    list = analysizeHtml(getHtmlText('https://rate.bot.com.tw/xrt?Lang=zh-TW'))
    text = json.dumps(list)
    print(text)