
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, precision_score, recall_score, f1_score, roc_curve, auc
from sklearn.linear_model import LogisticRegressionCV
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
import sklearn
import joblib
import boto3
import pathlib
from io import StringIO 
import argparse
import os
import numpy as np
import pandas as pd
    
def model_fn(model_dir):
    clf = joblib.load(os.path.join(model_dir, "model.joblib")) ###
    return clf
    
if __name__ == "__main__":

    print("[INFO] Extracting arguments")
    parser = argparse.ArgumentParser()

    # hyperparameters sent by the client are passed as command-line arguments to the script.
    parser.add_argument("--n_estimators", type=int, default=100)
    parser.add_argument("--random_state", type=int, default=0)

    # Data, model, and output directories
    parser.add_argument("--model-dir", type=str, default=os.environ.get("SM_MODEL_DIR"))
    parser.add_argument("--train", type=str, default=os.environ.get("SM_CHANNEL_TRAIN"))
    parser.add_argument("--test", type=str, default=os.environ.get("SM_CHANNEL_TEST"))
    parser.add_argument("--train-file", type=str, default="train-v1.csv")
    parser.add_argument("--test-file", type=str, default="test-v1.csv")

    args, _ = parser.parse_known_args()
    
    print("SKLearn Version: ", sklearn.__version__)
    print("Joblib Version: ", joblib.__version__)

    print("[INFO] Reading data")
    print()
    train_df = pd.read_csv(os.path.join(args.train, args.train_file))
    test_df = pd.read_csv(os.path.join(args.test, args.test_file))
    
    features = list(train_df.columns)
    label = features.pop(-1)
    
    print("Building training and testing datasets")
    print()
    X_train = train_df[features]
    X_test = test_df[features]
    y_train = train_df[label]
    y_test = test_df[label]

    print(features)

    print('Column order: ')
    print(features)
    print()
    
    print("Label column is: ",label)
    print()
    
    print("Data Shape: ")
    print()
    print("---- SHAPE OF TRAINING DATA (80%) ----")
    print(X_train.shape)
    print(y_train.shape)
    print()
    print("---- SHAPE OF TESTING DATA (20%) ----")
    print(X_test.shape)
    print(y_test.shape)
    print()
    
  
    print("Training RandomForest Model.....")
    print()
    # RF: 
    RFmodel = RandomForestClassifier(n_estimators=args.n_estimators, random_state=args.random_state, verbose = 3,n_jobs=-1)

    # GB: 
    # GBmodel =  GradientBoostingClassifier(n_estimators=args.n_estimators, random_state=args.random_state, learning_rate=0.1, 
    # max_depth = 3, min_samples_split=5, min_samples_leaf=2, subsample=0.8, verbose = 3)

    # LR and SVM: 
    # scaler = StandardScaler()
    # SVM_X_train_scaled = scaler.fit_transform(X_train)
    # SVM_X_test_scaled = scaler.transform(X_test)
    # # model = LogisticRegressionCV(Cs=10, cv=5, penalty='l1', solver='liblinear', random_state=42)
    # SVMmodel = SVC(kernel='rbf', C=1.0, random_state=42)

    # For RF and GB and SVM: 
    RFmodel.fit(X_train, y_train)
    # GBmodel.fit(X_train, y_train)
    # SVMmodel.fit(SVM_X_train_scaled, y_train) #don't fit/transform y!
    print()
    
    # save all to model.joblib (the same library)!
    # for sagemaker, simply change the desired model's to "model.joblib"
    # sagemaker only accepts one model as input
    # IF TREATING ENTIRE THING AS ONE MODEL, ONLY MAKE ONE PATH!
    RFmodel_path = os.path.join(args.model_dir, "model.joblib")
    joblib.dump(RFmodel,RFmodel_path)
    print("Combined Model persisted at " + RFmodel_path)
    print()
    # GBmodel_path = os.path.join(args.model_dir, "GBmodel.joblib")
    # joblib.dump(GBmodel,GBmodel_path)
    # print("GB Model persisted at " + GBmodel_path)
    # print()
    # SVMmodel_path = os.path.join(args.model_dir, "SVMmodel.joblib")
    # joblib.dump(GBmodel,GBmodel_path)
    # print("SVM Model persisted at " + SVMmodel_path)
    # print()

    
    # y_pred_test = model.predict(X_test_scaled)
    RF_y_pred_test = RFmodel.predict(X_test)
    RF_test_acc = accuracy_score(y_test,RF_y_pred_test)
    RF_test_rep = classification_report(y_test,RF_y_pred_test)

    # GB_y_pred_test = GBmodel.predict(X_test)
    # GB_test_acc = accuracy_score(y_test,GB_y_pred_test)
    # GB_test_rep = classification_report(y_test,GB_y_pred_test)

    # SVM_y_pred_test = SVMmodel.predict(SVM_X_test_scaled)
    # SVM_test_acc = accuracy_score(y_test,SVM_y_pred_test)
    # SVM_test_rep = classification_report(y_test,SVM_y_pred_test)

    #For LR and SVM: 
    # train_accuracy = model.score(X_train_scaled, y_train)
    # test_accuracy = model.score(X_test_scaled, y_test)    

    print()
    print("---- METRICS RESULTS FOR TESTING DATA ----")
    print()
    print("Total Rows are: ", X_test.shape[0])
    print('[TESTING] RF Model Accuracy is: ', RF_test_acc)
    print('[TESTING] RF Testing Report: ')
    print(RF_test_rep)
    print()
    # print('[TESTING] GB Model Accuracy is: ', GB_test_acc)
    # print('[TESTING] GB Testing Report: ')
    # print(GB_test_rep)
    # print()
    # print('[TESTING] SVM Model Accuracy is: ', SVM_test_acc)
    # print('[TESTING] SVM Testing Report: ')
    # print(SVM_test_rep)
    # print()

