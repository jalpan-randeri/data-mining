 [Required File Structure]
.
├── LR
│   ├── LR
│   │   ├── LR.iml
│   │   ├── lib
│   │   │   └── Jama-1.0.3.zip
│   │   └── src
│   │       ├── L2RegularizedLinearRegression.java
│   │       ├── Question1.java
│   │       ├── Question2.java
│   │       ├── Question3.java
│   │       ├── SplitFile.java
│   │       ├── conts
│   │       │   └── FileConts.java
│   │       ├── model
│   │       │   ├── InputTestSet.java
│   │       │   └── Question.java
│   │       └── utils
│   │           ├── FileUtils.java
│   │           ├── QuestionUtils.java
│   │           └── RandomUtils.java
│   └── out
└── hw1-data
    ├── 100(1000)_100_train.csv
    ├── 150(1000)_100_train.csv
    ├── 50(1000)_100_train.csv
    ├── test-100-10.csv
    ├── test-100-100.csv
    ├── test-1000-100.csv
    ├── train-100-10.csv
    ├── train-100-100.csv
    └── train-1000-100.csv



[Compiling]

javac -cp :LR/LR/lib/Jama-1.0.3.zip:LR/out/ -d LR/out LR/LR/src/model/*.java LR/LR/src/utils/*.java LR/LR/src/conts/*.java LR/LR/src/*.java




[Executing]

1. navigate to LR/LR/out

2. Execution for questions

2.1 run the following commands for Question1

java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question1 ./../../hw1-data/train-100-10.csv ./../../hw1-data/test-100-10.csv output_100_10.txt
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question1 ./../../hw1-data/train-100-100.csv ./../../hw1-data/test-100-100.csv output_100_100.txt
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question1 ./../../hw1-data/train-1000-100.csv ./../../hw1-data/test-1000-100.csv output_1000_100.txt
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question1 ./../../hw1-data/50\(1000\)_100_train.csv ./../../hw1-data/test-1000-100.csv output_50\(1000\)_100.txt
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question1 ./../../hw1-data/100\(1000\)_100_train.csv ./../../hw1-data/test-1000-100.csv output_100\(1000\)_100.txt
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question1 ./../../hw1-data/150\(1000\)_100_train.csv ./../../hw1-data/test-1000-100.csv output_150\(1000\)_100.txt


2.2 run the following commands for Question2

java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question2 1  ./../../hw1-data/train-1000-100.csv ./../../hw1-data/test-1000-100.csv output_lambda_1_1000_100.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question2 25  ./../../hw1-data/train-1000-100.csv ./../../hw1-data/test-1000-100.csv output_lambda_25_1000_100.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question2 150  ./../../hw1-data/train-1000-100.csv ./../../hw1-data/test-1000-100.csv output_lambda_150_1000_100.csv


2.3 run the following commands for Question3


java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question3  ./../../hw1-data/train-100-10.csv ./../../hw1-data/test-100-10.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question3  ./../../hw1-data/train-100-100.csv ./../../hw1-data/test-100-100.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question3  ./../../hw1-data/train-1000-100.csv ./../../hw1-data/test-1000-100.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question3  ./../../hw1-data/50\(1000\)_100_train.csv ./../../hw1-data/test-1000-100.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question3  ./../../hw1-data/100\(1000\)_100_train.csv ./../../hw1-data/test-1000-100.csv
java -cp :./../../LR/LR/lib/Jama-1.0.3.zip:out/ Question3  ./../../hw1-data/150\(1000\)_100_train.csv ./../../hw1-data/test-1000-100.csv
