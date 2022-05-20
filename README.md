# currency-converter
Sample app that uses public API to calculate currencies exchanges.

Sample request:

curl --location --request POST 'https://arcane-brook-74108.herokuapp.com/converter/exchange/queries' \
--header 'Content-Type: application/json' \
--data-raw '{
"source": "USD",
"target": "RUB",
"amount": 134.50
}'
