# Capital Bikeshare: Casual-to-Member Conversion Strategy
 
 Identifying high yield stations for a behavioral targeting campaign that converts casual riders into annual members.
 
**Live dashboard:** [Capital Bikeshare Conversion Target Explorer](https://public.tableau.com/app/profile/jack.hamilton4441/viz/Capital_Bikeshare_Conversion_Target_dashboard/Dashboard1)
 
**Deck:** [`Capital_bikeshare_presentation.pdf`]([capital_bikeshare.pdf](https://github.com/hamjac5334/Portfolio/blob/main/Data_Analysis/Capital_Bikeshare/capital_bikeshare.pdf))
**Full analysis notebook:** [`analysis.ipynb`](new.ipynb)
 
 
Analysis of **6.2 million Capital Bikeshare rides** over 12 months (May 2025 – April 2026) identifies **55,000 casual rides per month** (32% of casual ridership) that exhibit behavioral patterns indistinguishable from member ridership. Five stations in the upper Northwest DC corridor, anchored by Union Station and Georgetown University. A validated pilot campaign at these stations scales to an estimated **1,060 new annual members and $127K in annual recurring revenue** ($48K–$318K sensitivity range depending on conversion rate).
 
---
 
## The business question
 
**Client:** Capital Bikeshare Expansion Team
 
- **Situation.** Capital Bikeshare operates over 6 million trips per year across DC. Annual members generate predictable recurring revenue ($95–$120/year); casual riders pay per ride and represent roughly 30% of trips.
- **Complication.** Member growth is plateauing as paid acquisition costs rise. A meaningful share of casual rides exhibits repeat-commuter behavior — suggesting the conversion opportunity already exists inside the casual base but is invisible to current marketing.
- **Question.** Which 3–5 stations represent the highest-ROI targets for a casual to member conversion campaign?
- **Analytical pivot.** The public dataset contains no user IDs so analysis must operate at the ride level via behavioral signature rather than at the rider level via identity tracking.
---
 
## Headline finding
 
A gradient boosting classifier trained to distinguish member rides from casual rides identifies **55,226 casual rides per month** as behaviorally member-like (P(member) > 0.5). This is **five times the size** of a simple rule based commuter heuristic (rush hour + weekday + short + one way), which captures only 10,868 rides. The model-flagged population is the broader conversion target, riders who behave like members but haven't purchased the membership.
 
<img width="742" height="286" alt="Screenshot 2026-05-27 at 12 13 51 AM" src="https://github.com/user-attachments/assets/a4960332-9062-43e3-9070-e94f31fb7f01" />


Hour × day of week distribution of ride volume. Members ride in commute rhythm (weekday rush hours); casuals ride in leisure rhythm (weekend midday). The visible overlap at weekday rush hours is the conversion opportunity.
 
---
 
## Recommended targets
 
Five stations rank highest by **target score** (model flagged member like volume × concentration of member like behavior):
 
| Rank | Station | Monthly member-like rides | Concentration | Total casual rides |
|------|---------|---------------------------|---------------|--------------------|
| 1 | Columbus Circle / Union Station | 528 | 44% | 1,206 |
| 2 | 14th & Irving St NW | 490 | 59% | 826 |
| 3 | New Hampshire Ave & T St NW | 465 | 43% | 1,078 |
| 4 | 37th & O St NW / Georgetown University | 407 | 51% | 798 |
| 5 | 14th & V St NW | 380 | 40% | 955 |
 
These stations cluster geographically along the 14th Street / U Street / Logan Circle / Dupont corridor in upper Northwest DC, with two transit/university anchors (Union Station, Georgetown). The clustering enables a single corridor-level campaign rather than five independent station-by-station efforts.

<img width="861" height="342" alt="Screenshot 2026-05-27 at 12 00 44 AM" src="https://github.com/user-attachments/assets/df7a014a-7c76-40a2-9542-e82e53cc9c18" />

 
<img width="1463" height="747" alt="Screenshot 2026-05-26 at 11 57 32 PM" src="https://github.com/user-attachments/assets/eddeff49-39d1-49b5-a7c1-943faa50e4e8" />


Station level conversion potential. Marker color encodes member-like share; marker size encodes total casual ride volume
 
---
 
## Recommended interventions
 
Four tactics, each tied to a feature the model identified as most predictive of member-like behavior:
 
1. **On bike screen messaging during weekday rush hours.** Contextual offer ("Save up to $270/year if you are already riding 3+ times a week?") on bike displays at the five target stations, 7–9am and 4–7pm weekdays. Estimated reach: 1,400 rush hour impressions per station per month at zero incremental cost.
2. **Permanent dock signage with QR value calculator.** Above dock signage at each target station linking to a savings calculator. 24/7 visibility, reuses the rush hour creative.
3. **Trigger based email and push notifications.** Send membership offers to accounts purchasing 3+ single-trip passes within 30 days, operationalizes the conversion insight via transaction data.
4. **Adjacent employer and university partnerships.** Pursue corporate-rate pilots with HR offices at Georgetown University and Union Station's federal employer cluster.
---
 
## Sizing the opportunity
 
| Scope | New annual members | Annual recurring revenue |
|---|---|---|
| Pilot (5 stations) | 43 | $5,200 |
| Full systemwide | 1,060 | $127,000 |
 
**Sensitivity to conversion rate:**
 
| Conversion rate | New members | Revenue |
|---|---|---|
| 3% | 398 | $47,760 |
| 5% | 663 | $79,560 |
| **8% (baseline)** | **1,060** | **$127,200** |
| 12% | 1,591 | $190,920 |
| 15% | 1,988 | $238,560 |
| 20% | 2,651 | $318,120 |
 
The pilot is the validation step that narrows the systemwide range. Sizing assumes 30 rides per unique potential converter per year, a key assumption that field testing should validate.
 
---
 
## Pilot design
 
| Component | Detail |
|---|---|
| **Target stations** | The 5 stations listed above |
| **Control stations** | 3 matched stations (similar volume, casual share, geographic context) |
| **Window** | 90 days |
| **Success metric** | ≥20% lift in casual to member conversion rate at p < 0.05 |
| **Power** | 25 conversions per arm required to detect the effect |
 
---
 
## Approach
 
The analysis follows a five stage pipeline:
 
1. **Data.** 6.2M ride records across 12 months and 500 stations.
2. **Cleaning.** 2.3% of rides dropped (duration outliers, negative or sub-1-minute trips, > 24-hour trips). Systematic data quality audit with documented treatment for each issue.
3. **Feature engineering.** 17 engineered features across three families: temporal (hour, day-of-week, rush-hour, season), trip-level (duration, distance, speed, round-trip flag, bike type), and station aggregates (per-station ride totals, casual share, avg duration).
4. **Modeling.** Chronological train/validation/test split (months 1–10 train, 11 validation, 12 test). HistGradientBoostingClassifier with class balancing. Target leakage controlled by computing station aggregates on training data only and merging onto val/test.
5. **Translation.** Model scores aggregated to the station level. Stations ranked by member like volume × concentration. Sizing computed transparently with named, sensitivity tested assumptions.
**Model performance.** ROC AUC = 0.73 on the held out validation set. The AUC reflects real overlap between member and casual ride patterns, which is the central thesis of the analysis. Effect sizes (Cramér's V = 0.21 for rush-hour effect) confirm that the differences are practically meaningful, not artifacts of sample size.
 
 
## Reproducing the analysis
 
The raw data (12 monthly CSVs, 6.2M rows total) is not committed to this repo, but it's publicly available from Capital Bikeshare and would exceed GitHub file limits. To reproduce:
 
1. **Clone the repository.**
   ```bash
   git clone https://github.com/hamjac5334/Portfolio.git
   cd Portfolio/capital-bikeshare-conversion
   ```
 
2. **Set up the Python environment.**
   ```bash
   python -m venv .venv
   source .venv/bin/activate   # on macOS/Linux
   pip install -r requirements.txt
   ```
 
3. **Download the data.** Get the 12 monthly trip CSVs (May 2025 through April 2026) from [Capital Bikeshare's public data feed](https://capitalbikeshare.com/system-data) and place them in a `data/` folder at the project root. File naming convention: `YYYYMM-capitalbikeshare-tripdata.csv`.
4. **Run the notebook.** Open `analysis.ipynb` in Jupyter and run all cells. Expected runtime: 3–5 minutes on a modern laptop.
---
 
## Limitations
 
- **No user IDs.** The public dataset records rides, not riders. Analysis operates at the ride level and assumes ride patterns reflect underlying rider behavior. Sizing assumes 30 rides per unique potential converter annually.
- **Moderate model AUC (0.73).** Targeting rankings should be field-validated against a control set before broad rollout.
- **Feature dependence.** The top model features (`speed`, `station level casual share`) encode a mix of behavioral and geographic signal. Station level casual share in particular captures historical base rate, true behavioral conversion candidates outside high casual stations may be undercounted.
- **Geographic concentration risk.** The pilot is concentrated in upper Northwest DC; weather, local events, or station outages during the pilot window could distort the read.
---
 
## What I'd do with more time
 
- Acquire anonymized user level data to enable rider-level (not ride-level) conversion modeling.
- Add more in depth features: weather, event calendar, station amenities, adjacent transit ridership.
- Hyperparameter tuning and out of time cross validation.
- Operationalize the model as a real time targeting system feeding the on-bike screens and trigger based notifications.
- A/B test the four interventions individually to attribute conversion lift to specific tactics.
---
 
## Tools and stack
 
- **Python:** pandas, numpy, scikit-learn, scipy, matplotlib, seaborn, folium
- **Modeling:** HistGradientBoostingClassifier (sklearn), Welch's t-test, chi-square, effect sizes (Cohen's d, Cramer's V)
- **Visualization:** matplotlib/seaborn for notebook charts, folium for interactive maps, Tableau Public for the live dashboard
- **Deliverables:** Jupyter, PowerPoint, Tableau
