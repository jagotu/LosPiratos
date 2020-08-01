websocat ws://localhost:4242 | while IFS= read line;
do
    if [ $line == refresh ]
    then
        date
        ./status.sh
        echo
    fi
done
