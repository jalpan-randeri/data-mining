create out directory

[Directory Structure]

.
├── input
│   ├── test.arff
│   └── train.arff
├── out
└── src
    ├── KNN.java
    ├── comparators
    │   └── PredictorComparator.java
    ├── model
    │   ├── MinMax.java
    │   ├── Prediction.java
    │   └── Question.java
    └── utils
        └── FileUtils.java


[Compilation]

javac -cp . src/model/*.java src/comparators/*.java src/utils/*.java src/*.java -d out


[Running]

java KNN ../input/train.arff ../input/test.arff

