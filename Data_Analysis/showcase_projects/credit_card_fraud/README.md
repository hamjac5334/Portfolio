# Credit Card Fraud Detection Analysis

Building a machine learning pipeline to detect fraudulent credit card transactions using SQL, feature engineering, resampling, and classification models on the Capital One fraud dataset.  

**Data Used** https://github.com/CapitalOneRecruiting/DS 


**Full Notebook** [https://github.com/hamjac5334/Portfolio/blob/main/Data_Analysis/showcase_projects/credit_card_fraud/creditcardfraud.ipynb](creditcardfraud.ipynb)

## Research question

**Client:** Fraud analytics / risk team

**Situation.** Credit card fraud is a major problem in digital payment systems and costs financial institutions significant time and money to detect and resolve.

**Complication.** The dataset is highly imbalanced, with fraud making up less than 2% of all transactions, so a naive model would mostly learn to predict the majority class.

**Question.** Can a machine learning pipeline using SQL, feature engineering, and resampling accurately detect fraud without overwhelming false positives?

## Headline finding

Fraud detection is possible, but the class imbalance makes the task difficult.  
After cleaning the data, engineering features, and applying SMOTE or class weighting, the best models improved fraud recall, but false positives remained a major issue.

## Key findings

- The dataset is heavily imbalanced, with far fewer fraudulent transactions than legitimate ones.
- <img width="790" height="590" alt="image" src="https://github.com/user-attachments/assets/7bb1ee7a-59ef-4e7f-941a-2288216887f9" />

- Splitting the data into separate users and transactions tables made the SQL workflow cleaner and easier to manage.
- Dropping redundant features, such as `posentrymode` after using `cardpresent`, helped reduce overlap in the feature set.
- SMOTE was applied only to the training data to avoid data leakage.
- Random Forest and XGBoost both improved fraud detection, but precision for the fraud class remained low.

## Analytical approach

The project follows an end-to-end workflow:

1. Download the Capital One fraud dataset.
2. Set up a PostgreSQL database in pgAdmin.
3. Split the raw data into relational `users` and `transactions` tables.
4. Perform SQL EDA and data quality checks.
5. Clean duplicates, handle missing values, and remove obvious anomalies.
6. Engineer fraud related features.
7. Address class imbalance with SMOTE and class weights.
8. Train and evaluate Random Forest and XGBoost models.

## Data preparation

The data was stored in PostgreSQL using separate tables for users and transactions to improve readability and keep static account data apart from high volume transaction data.  
A synthetic `uniquekey` was created to preserve relational integrity and make joining tables easier.  
The transaction table included fields such as `transactionamount`, `merchantcategorycode`, `cardpresent`, and `isfraud`, while the users table held account level details like credit limit and available money.

## Feature engineering

Several features were created to improve fraud detection performance.  
These included `hour`, `historicalmedian`, `usertransactionpctdiff`, `timebetweenlasttransaction`, `highriskcat`, `mismatchedcvv`, and `isaddressverification`.  
Some features were removed because they duplicated information already captured elsewhere, such as `posentrymode` after including `cardpresent`.

<img width="996" height="593" alt="image" src="https://github.com/user-attachments/assets/5b929cbb-f1d5-4f47-80d6-459dcbb21392" />


## Modeling

The project compared multiple classification approaches, including Random Forest and XGBoost.  
Because fraud was so rare, the training set was balanced with SMOTE in some experiments and class weighting in others.  
The notebook also used group-based splitting by customer to reduce leakage across train and test sets.  
Model tuning was performed with cross-validation and randomized search.

<img width="790" height="590" alt="image" src="https://github.com/user-attachments/assets/6f15e2d8-262f-44cf-b7d5-a5c3e74598fc" />


## Model performance

| Model | Fraud Precision | Fraud Recall | Fraud F1 | Notes |
|---|---:|---:|---:|---|
| Random Forest | 0.03 | 0.64 | 0.06 | Better at finding fraud than a baseline model, but it produced many false positives. |
| XGBoost | 0.04 | 0.50 | 0.08 | Improved overall accuracy and reduced false positives, but precision for fraud remained weak because of the imbalance. |


## Business impact

This kind of model is useful because missing fraud is expensive, but false alarms are also costly.  
A strong fraud system needs to balance fraud detection with customer experience, not just maximize raw accuracy.  
That is why fraud recall, precision, and false-positive control matter more than accuracy alone in this project.

## Limitations

This analysis is based on a limited dataset and does not include every feature that a production fraud system would use.  
The class imbalance is extreme, which makes the problem much harder and depresses precision for the fraud class.  
Some important signals may still be missing, including geolocation patterns, device data, and behavioral history.  
The notebook is also exploratory rather than a fully deployed production fraud system.

## What I’d do next

- Add threshold tuning to reduce false positives.
- Compare SMOTE against class weighting more systematically.
- Try anomaly detection models alongside supervised classifiers.
- Add time based validation to better reflect production use.
- Test more feature interactions, especially between merchant risk and transaction behavior.

## Tools and stack

- Python: `pandas`, `numpy`, `matplotlib`, `seaborn`, `scikit-learn`, `xgboost`
- Resampling: `SMOTE`, class weighting
- Database: PostgreSQL, SQLAlchemy, pgAdmin
- Modeling: Random Forest, XGBoost, randomized search, group based train/test split
- Data source: Capital One fraud detection dataset
