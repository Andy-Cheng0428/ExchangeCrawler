# coding=utf-8
import pandas as pd

dfs = pd.read_html('https://www.yuantabank.com.tw/bank/exchangeRate/hostccy.do')
currency = dfs[0]

currency = pd.DataFrame(currency)
#print(currency.info()) 可印出dataFrame資訊
currency = currency.rename(columns={'幣別':'curr','即期買入':'spotBuying','即期賣出':'spotSelling','現鈔買入':'cashBuying','現鈔賣出':'cashSelling'}) #更換column標頭名稱
currency = currency.astype({'cashBuying':'str', 'cashSelling':'str','spotBuying':'str','spotSelling':'str'})
print('YUANTA')
print(currency[['curr','spotBuying','spotSelling','cashBuying','cashSelling']].to_json(None, 'records', None, 10, False))
