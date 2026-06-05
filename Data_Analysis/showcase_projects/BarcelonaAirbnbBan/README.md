# Barcelona 2028: Airbnb Ban Impact Analysis

Predicting the economic and housing impact of Barcelona's 2028 Airbnb ban using historical data from NYC and Lisbon as natural experiments.

**Live dashboard:** [Barcelona 2028 Airbnb Ban Impacts](https://public.tableau.com/app/profile/jack.hamilton4441/viz/BarcelonaAirbnb_17806215055810/Dashboard1?publish=yes)

**Full analysis notebook:** [`barcelona.ipynb`](barcelona.ipynb)

Analysis of **124,447 Airbnb listings** across Barcelona, New York City, and Lisbon reveals that accommodation bans consistently raise — not lower — nightly prices in both treated cities. NYC saw a **14.5% price increase** post-Local Law 18; Lisbon saw a **35.5% increase** following its moratorium. A Difference-in-Differences model trained on this evidence predicts Barcelona will face a **44.88% average price increase** across all neighborhoods post-ban, with Eixample projected to exceed **$305/night**. The evidence supports rejecting a full ban in favor of targeted neighborhood-level STR caps combined with strategic hotel development.


## The research question

**Client:** Ajuntament de Barcelona

- **Situation.** Barcelona hosted 24 million visitors in 2023, 1,100% of its 2 million residents. Short-term rental platforms like Airbnb have driven landlords to convert residential units to tourist use, pricing locals out of neighborhoods where they have lived their entire lives. The city mayor has announced a full ban on Airbnb tourist apartment licenses effective November 2028.
- **Complication.** Tourism represents a critical share of the Catalonian economy. Evidence from other cities suggests that banning STRs raises accommodation costs without reliably lowering local housing prices, potentially harming both tourists and residents simultaneously.
- **Question.** Should Barcelona execute the 2028 ban as planned, and if so, what complementary measures are needed to protect both housing affordability and tourism viability?
- **Analytical approach.** Use NYC (Local Law 18, September 2023) and Lisbon (moratorium 2021, reversed 2024) as natural experiments. Train a Difference-in-Differences model on pre/post ban data from both cities to predict Barcelona's post-2028 trajectory.



## Headline finding

Every Barcelona neighborhood is predicted to see price increases post-ban, ranging from **+15% in Nou Barris to +56% in Eixample**. This is consistent with the historical record: neither NYC nor Lisbon saw local rent prices fall after implementing bans — both cities saw accommodation prices rise instead, suggesting the ban's mechanism (reducing STR supply) does not translate into lower housing costs for residents.

| Neighborhood | Pre-Ban Avg | Predicted Post-Ban | Increase |
|---|---|---|---|
| Eixample | $195 | $305 | +56% |
| Gràcia | $160 | $236 | +47% |
| Ciutat Vella | $131 | $201 | +53% |
| Horta-Guinardó | $115 | $144 | +25% |
| Nou Barris | $109 | $125 | +15% |



## Recommendation

Barcelona should not execute a full Airbnb ban in 2028 without complementary measures. Based on NYC and Lisbon evidence, the ban will cause significant accommodation price increases for tourists while failing to lower housing costs for locals — as landlords respond to reduced STR demand by maintaining rents rather than lowering them.

As an alternative, Barcelona should implement **neighborhood level STR caps** targeting the most saturated districts (Eixample at 1,255 listings/km², Ciutat Vella at 1,442 listings/km²) while investing in hotel capacity in high tourism areas. This approach reduces housing displacement pressure without eliminating tourist accommodation entirely, preserving the tourism economy while protecting residential neighborhoods.


## Key analytical findings

**Density != Price.** Ciutat Vella — the most tourist saturated neighborhood at 1,442 listings/km^2, has below average Airbnb prices ($131/night vs. $144 city average). Competition in the most saturated areas suppresses prices; scarcity in residential neighborhoods like Eixample drives them up. This challenges the assumption that high Airbnb concentration directly causes housing price increases.

**Bans raise accommodation costs.** The DiD coefficient of +$37 (controlling for city, neighborhood, room type, and density) confirms that bans consistently raise accommodation prices across all market contexts.

**Entire homes dominate Barcelona's STR market.** 72% of Barcelona listings are entire home/apt type, the category most likely to displace local residents. This is higher than NYC (55%) and Lisbon (71%), suggesting Barcelona's housing displacement risk is real but that targeted restrictions on entire home listings could be more effective than a blanket ban.


## Approach

The analysis follows a five stage pipeline:

1. **Data acquisition.** Inside Airbnb listings data for Barcelona (2 quarterly snapshots, 2025), NYC (pre/post Local Law 18), and Lisbon (pre-moratorium 2021, post-reversal 2025). Neighborhood population and area data from Open Data BCN, INE Spain, and INE Portugal. Total: 124,447 cleaned listings across 3 cities.

2. **SQL cleaning.** PostgreSQL database with 3 relational tables (listings, neighborhoods, cities). Outlier removal with log transformation 2 standard deviation filter by city. $0 price removal. Data quality audit across all columns.

3. **EDA.** Geographic density mapping, pre/post ban price comparison across cities, neighborhood price distribution by room type.

4. **Feature engineering.** Binary `is_post_ban` treatment variable, city level median price, neighborhood average price, listings per km^2, listings per capita. One hot encoding for room type.

5. **Modeling.** Difference-in-Differences framework via three models (Linear Regression, Random Forest, XGBoost) trained on NYC + Lisbon data. Barcelona synthetic post-ban rows generated by flipping `is_post_ban = 1` on all Barcelona listings. Sample weights applied to correct for city imbalance (NYC 55%, Lisbon 32%, Barcelona 24%).

**Model performance** (Random Forest, best performer):
- MAE: $56.46
- R^2: 0.45

R^2 of 0.45 is consistent with published STR price prediction research using neighborhood level features. Individual listing prices are influenced by photo quality, host reviews, and amenities not captured in this dataset. The directional finding (prices rise post-ban) was consistent across all three model types.


## Natural experiment cities

| City | Policy | Cutoff Date | Outcome |
|---|---|---|---|
| New York City | Local Law 18 (near total STR ban) | September 5, 2023 | -44% listings, +14.5% prices |
| Lisbon | STR moratorium, subsequently reversed | June 2021 / reversed 2024 | +35.5% prices post-restriction |
| Barcelona | Full Airbnb ban (planned) | November 2028 | Predicted: +44.88% avg price |


## Limitations

- **No local rent price data.** The model predicts Airbnb accommodation prices, not local housing rents. The connection between STR supply and residential rents is inferred from NYC and Lisbon outcomes rather than modeled directly.
- **Barcelona is all pre-ban.** There is no post 2028 Barcelona data, predictions are extrapolations from other cities' experiences under the assumption that Barcelona's market dynamics are comparable.
- **Lisbon pre-ban data unavailable.** The earliest Lisbon snapshot (December 2021) is six months post-moratorium. The "pre-ban" Lisbon comparison uses early ban data as a baseline, which may understate the true pre-ban price level.
- **Moderate R^2 (0.45).** Listing level price variance driven by photos, reviews, and amenities is not captured by neighborhood level features.
- **City comparability assumptions.** NYC and Lisbon differ from Barcelona in housing market structure, regulatory environment, and tourism type. The DiD model assumes these differences are captured by city level fixed effects.


## What I'd do with more time

- Incorporate local residential rent data (INCASOL, Idealista) to model housing affordability directly rather than inferring it from STR market outcomes.
- Add more cities with clean ban cutoff dates (Amsterdam, Florence) to strengthen the DiD training signal.
- Extend feature set with listing level review scores and availability data from Inside Airbnb's calendar files.
- Build a neighborhood level simulation tool allowing the Ajuntament to model outcomes under different cap thresholds rather than a binary ban/no-ban decision.
- Conduct spatial autocorrelation analysis to test whether high density STR neighborhoods spill over into adjacent residential areas.


## Tools and stack

- **Python:** pandas, numpy, scikit-learn, xgboost, matplotlib, seaborn
- **Database:** PostgreSQL with SQLAlchemy
- **Modeling:** LinearRegression, RandomForestRegressor, XGBRegressor (sklearn/xgboost), Difference-in-Differences framework
- **Visualization:** matplotlib/seaborn for notebook charts, Tableau Public for live dashboard
- **Data sources:** Inside Airbnb, Open Data BCN (Ajuntament de Barcelona), INE Spain, INE Portugal
