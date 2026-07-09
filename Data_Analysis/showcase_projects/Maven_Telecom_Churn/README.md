# Telecom Customer Churn Prediction & Retention Strategy
Predicting which telecom subscribers are likely to churn and tuning the model's decision threshold to match the real cost asymmetry a retention team faces, using Maven Analytics' Customer Churn dataset.
 
**Full analysis notebook:** [`churn.ipynb`](churn.ipynb)
 
Analysis of **7,043 California telecom subscribers** (Q2 2022) shows churn is heavily concentrated among month-to-month contract holders (46% churn rate, vs. 2.5% for two-year contracts) and customers paying by mailed check or bank withdrawal rather than credit card. A tuned XGBoost classifier, benchmarked against Logistic Regression and Random Forest baselines, reaches **87% accuracy** and an **F1 of .78** on the churn class. Because missing a churner costs the business more than an unnecessary retention offer, the classification threshold was deliberately shifted to prioritize recall, **catching 82% of churners** at a precision cost of .74, a tradeoff chosen to match how the model would actually be used by a retention team.
 
## The research question
 
**Client:** Maven's Marketing/Retention Team
 
- **Situation.** The company sells subscription based phone, internet, and streaming services. Roughly a quarter of the customer base churns each quarter, and retention campaigns (discounts, offers) cost real money to run.
- **Complication.** Retention budgets are limited. Offering a retention discount to a customer who was never going to leave wastes money; failing to flag a customer who was about to leave loses that customer's revenue entirely, a more expensive mistake than the wasted offer. Any model built for this has to reflect that the two error types are not equally costly.
- **Question.** Which customers are likely to churn this quarter, and how should the marketing team prioritize retention outreach given that missing a churner is worse than over-targeting?
- **Analytical approach.** Train a classifier on historical churn outcomes, correct for class imbalance and the false-negative/false-positive cost asymmetry during training, and tune the decision threshold explicitly around that cost tradeoff rather than defaulting to 0.5.
<!-- add screenshot: SQL EDA churn-rate-by-contract chart or dashboard, if available -->
 
## Headline finding
 
Contract type is the single strongest lever: month-to-month customers churn at more than 18x the rate of two year contract holders. Competitor offers, not service dissatisfaction or price alone, are the leading stated reason for leaving — suggesting retention efforts need to compete on offers, not just fix service quality.
 
| Contract Type | Customers | Churn Rate |
|---|---|---|
| Month-to-Month | 3,610 | 45.8% |
| One Year | 1,550 | 10.7% |
| Two Year | 1,883 | 2.5% |
 
| Payment Method | Churn Rate |
|---|---|
| Mailed Check | 36.9% |
| Bank Withdrawal | 34.0% |
| Credit Card | 14.5% |
 
## Recommendation
 
Retention spend should be prioritized using the model's risk scores at the tuned threshold (0.35), which is calibrated to catch more at-risk customers at some cost to precision — appropriate given that a missed churner is more costly than a wasted offer. Beyond individual targeting, the strongest structural lever is contract conversion: incentivizing month-to-month customers onto annual contracts addresses the single biggest churn driver directly, rather than relying on reactive retention offers alone. Given that "Competitor" is the leading churn reason, retention offers should be benchmarked against competitor pricing, not just framed as loyalty discounts.
 
## Key analytical findings
 
**Class imbalance shapes the whole modeling approach.** Only 26.5% of customers churned in the dataset. Training with default class weights would bias the model toward the majority (stayed) class, so `scale_pos_weight` was tuned via cross-validated search rather than left at its default, and the classification threshold was separately adjusted after training to reflect the cost asymmetry.
 
**Competitor pressure, not dissatisfaction, drives the most churn.** 45% of churned customers cited a competitor as their reason for leaving, more than price (11%), dissatisfaction (17%), and attitude (17%) combined don't reach that share.
 
**Payment method is a meaningful, underused signal.** Customers on bank withdrawal or mailed check churn at more than double the rate of credit card payers, independent of contract type, a low effort screening signal for risk.
 
**Tenure, contract type, and referral count are the strongest predictors.** SHAP values and permutation importance independently agree on this ranking, reinforcing that newer customers on flexible contracts with no referral history are the highest risk group.
 
## Approach
 
The analysis follows a five stage pipeline:
 
1. **Data acquisition & schema design (SQL).** Maven's Telecom Customer Churn dataset merged with zipcode population data, normalized into three relational PostgreSQL tables (`customer_info`, `plan_details`, `financial`) joined on customer ID.
2. **SQL EDA.** Churn rate and spend distribution by customer status, payment method, churn category, and contract type, computed directly with SQL aggregation and window functions.
3. **Python EDA.** Distribution and relationship charts (monthly charge by status, contract by payment method, churn category breakdown, spend heatmaps) to cross-validate the SQL findings visually.
4. **Data cleaning & feature engineering.** Removed rows with negative monthly charges (a billing data quality issue) and excluded the "Joined" cohort (customers with no full-quarter status, whose inclusion would conflate new sign ups with genuine retention). Engineered `high_price_rate`, `customer_term`, and `charges_per_term` features; one hot encoded categorical fields.
5. **Modeling.** XGBoost tuned via `RandomizedSearchCV` with `StratifiedKFold` cross-validation and early stopping, with `scale_pos_weight` searched rather than assumed. Decision threshold selected from the precision-recall curve to match the false negative cost priority. Benchmarked against Logistic Regression and Random Forest baselines on identical features. Interpreted using both SHAP and permutation importance to cross check feature rankings.
**Model performance** (tuned XGBoost, threshold = 0.35):

<img width="989" height="590" alt="image" src="https://github.com/user-attachments/assets/5bcd28a5-b8b9-47bd-a7df-1f218167de33" />

<img width="781" height="547" alt="image" src="https://github.com/user-attachments/assets/877dc79a-73b5-46bf-9eef-f6cd9bcfae57" />


 
- Accuracy: 87%
- Churn class: Precision .74, Recall .82, F1 .78
- Retained class: Precision .92, Recall .89, F1 .91
- PR-AUC: .88, ROC-AUC: .945
- Outperforms Logistic Regression (F1 .71) and Random Forest (F1 .73) baselines trained on the same features

<img width="870" height="497" alt="image" src="https://github.com/user-attachments/assets/8ad7dd8c-4a5e-4cd7-b054-dc3e6b5dbc53" />

<img width="547" height="432" alt="image" src="https://github.com/user-attachments/assets/34f4f9d7-0f25-48aa-963f-d4c476f1e78b" />

 
## Limitations
 
- **Single quarter, single region.** The dataset is a California (Q2 2022); generalization to other regions or time periods is untested.
- **Uncalibrated probabilities.** Class weighting used to correct for imbalance distorts the model's raw output away from true likelihoods,the scores are reliable for *ranking* risk but shouldn't be read as literal churn probabilities without recalibration.
- **Some remaining overfitting.** Train F1 on the churn class (.80–.93 depending on the split) runs ahead of test F1 (.78), indicating the model has room to generalize better.
- **"Joined" customers are excluded from training.** New customers without a full quarter status aren't scored by this model; a production version would need a separate approach for early tenure risk.
- **Churn is defined as non-renewal at a single snapshot**, not a continuously observed cancellation event, so the lead time between a rising risk score and actual churn isn't directly measured here.
## What I'd do with more time
 
- Recalibrate predicted probabilities (Platt scaling or isotonic regression) so scores reflect true likelihoods rather than outputs distorted by class weighting.
- Validate the model on additional quarters or regions to test whether the churn drivers generalize beyond this one snapshot.
- Incorporate customer lifetime value or subscription revenue into prioritization, so retention spend is ranked by expected value at risk, not churn probability alone.
- Replace the eyeballed precision-recall threshold with one derived from actual retention campaign cost and average customer value.
- Run the recommended retention strategy as a controlled test (holdout or randomized rollout) to measure whether targeted offers actually reduce churn, not just whether the model predicts it accurately.
## Tools and stack
 
- **Python:** pandas, numpy, scikit-learn, xgboost, shap, matplotlib, seaborn
- **Database:** PostgreSQL with SQLAlchemy
- **Modeling:** XGBoost (tuned via `RandomizedSearchCV` + `StratifiedKFold`), Logistic Regression and Random Forest baselines
- **Interpretability:** SHAP, permutation importance
- **Data source:** Maven Analytics Customer Churn Challenge (Telecom dataset)
