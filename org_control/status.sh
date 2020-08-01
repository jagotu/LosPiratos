echo -n "Číslo kola: "
curl -s localhost:8001/game | jq .roundNo

echo -n "Další kolo: "
timestamp=$(curl -s localhost:8001/roundend)
if [ $timestamp -eq -1 ]
then
    echo "Časovač neběží"
else
    date -d "@$timestamp" | tr -d '\n'
    curr=$(date +%s)
    diff=$(($timestamp-$curr))
    echo -n " (za "
    date -u -d @"$diff" +'%-Mm %-Ss' | tr -d '\n'
    echo ")"
fi

timerlength=$(curl -s "localhost:8001/org/timerLength?token=$TOKEN")
echo -n "Délka kola: $timerlength ("
date -u -d @"$timerlength" +'%-Mm %-Ss' | tr -d '\n'
echo ")"


