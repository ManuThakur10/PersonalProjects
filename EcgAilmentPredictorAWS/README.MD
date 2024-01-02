Hi everyone! This is my ECG ailment predictor I designed. This took a lot of work, so if you do happen to stumble across this and want to use it, please remember to cite my name.

I did all my data prep and analysis in the "ECG_prepAndAnalysis.ipynb" file, while everything concerning the models and AWS deployment was done in the "Sagemaker SKlearn.ipynb" file.

Note: to get started with this project, you will first need to set up an AWS virtual environment! Follow these steps: 
1. Install the AWS CLI: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
2. Create an AWS account and go to IAM. Then, create a user with administrator access.
3. Create a CLI access key for this user. Make sure to keep the access and secret access keys somewhere!
4. Then, in your terminal, type: "aws configure" (without the strings around it)
5. Log in with your keys and region (ex: "us-west-1"), don't put anything for output (just hit enter)
6. Then, open a blank jupyter notebook in your IDE
7. To create the virtual env, type the following command in terminal: "conda create -p myenv python=3.8"
8. Then, to activate the virtual env, type the following command in terminal: "conda activate myenv /\"
9. Use pip to install all imports in requirements.txt
10. You're ready to start!
