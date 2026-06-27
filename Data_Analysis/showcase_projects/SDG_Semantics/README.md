# Semantic Evaluation of UN SDG Reports Over Time
Analyzing how the sentiment, urgency, and messaging of UN Sustainable Development Goal reports have changed from 2016 to 2025 using NLP and multi label zero shot classification.
 
**Full analysis notebook:** [`un-sdg.ipynb`](un-sdg.ipynb)
 
Analysis of **3 UN SDG reports** (2016, 2021, 2025) across **3,000+ sentence-SDG label pairs** reveals that urgency language peaked in 2021, consistent with COVID-19's disruption of global progress, and has partially recovered in 2025, but remains more urgent than the inaugural 2016 report. A Spearman correlation of **r = -0.356 (p = 0.039)** confirms that goals with worse actual progress receive measurably more urgent language, validating the urgency metric against official UN progress ratings. Reduced Inequalities was the only SDG to rank in the top 3 most urgent goals across all three reports.
 
## The research question
 
- **Situation.** The United Nations publishes annual SDG progress reports assessing global advancement toward 17 interconnected goals set for 2030. The tone, emphasis, and urgency of these reports shift year over year as the geopolitical and environmental landscape changes.
- **Complication.** It is unclear whether the language of the reports tracks actual goal progress, or whether the UN adjusts its messaging independently of real results. COVID-19, armed conflict, and climate change have all disrupted the trajectory since 2016.
- **Question.** Has the sentiment and urgency of UN SDG reports changed meaningfully from 2016 to 2025, and does more urgent language correlate with worse actual progress?
- **Analytical approach.** Extract and tokenize sentences from three annual reports. Apply VADER sentiment analysis for urgency scoring. Use `facebook/bart-large-mnli` zero shot classification to assign each sentence to an SDG goal. Cross reference urgency scores against official UN progress ratings via Spearman correlation.
  
<img width="1045" height="523" alt="Screenshot 2026-06-27 at 10 55 43 AM" src="https://github.com/user-attachments/assets/c9d3d9e0-0352-4920-b815-6ce3641d14af" />

 
## Headline findings
 
Urgency language peaked in 2021 and the overall tone of reports has become more consistent and less variable over time, converging around a moderately positive score across all goals by 2025. Goals with the worst actual progress, such as Reduced Inequalities, Life Below Water, and Good Health, consistently received the most urgent language, with a statistically significant negative correlation between urgency score and UN progress rating (r = -0.356, p = 0.039).
 
| Year | Mean Urgency Score | Pattern |
|---|---|---|
| 2016 | -0.233 | High variation, hyper-positive on most goals |
| 2021 | -0.139 | Most urgent year; COVID-19 effect visible |
| 2025 | -0.186 | Consistent moderate urgency across all goals |
 
*Note: Urgency = −VADER compound score. Positive = more urgent/dire language.*
 
<img width="996" height="593" alt="Screenshot 2026-06-27 at 10 59 25 AM" src="https://github.com/user-attachments/assets/3362d144-62a2-4c82-81dc-f77f7e1f9aa3" />

<img width="813" height="600" alt="Screenshot 2026-06-27 at 11 03 25 AM" src="https://github.com/user-attachments/assets/d225451a-bdc9-469d-8bc6-3045c81c29ae" />

 
## Key analytical findings
 
**Urgency correlates with actual progress.** A Spearman r of -0.356 (p = 0.039) confirms that goals receiving more urgent language are the same goals the UN officially rates as stagnating or regressing. The text is not just rhetoric, it replicates reality.
 
**2021 was the most urgent year.** COVID-19 disrupted progress across nearly all goals simultaneously, and the 2021 report's language reflects this. Goals like No Poverty and Zero Hunger, showed the sharpest spike in urgency relative to 2016 and 2025. This could be attributed to COVID and the global concerns that the pandemic brought.
 
**Two distinct goal clusters emerged.** Hierarchical clustering of urgency profiles reveals environmental and social goals (Climate Action, Gender Equality, Clean Water) moved in lockstep over the decade, while poverty and infrastructure goals (No Poverty, Zero Hunger, Industry and Innovation) followed a separate trajectory shaped by COVID-19 and geopolitical disruption.
 
**Reduced Inequalities is persistently urgent.** It was the only goal that ranked in the top 3 most urgent SDGs in all three reports. This reflects bothglobal concern and a tendency of the zero shot model to over classify this label due to its broad semantic scope.
 
**The classifier overclassifies Reduced Inequalities.** Manual evaluation of 20 sentences revealed that the model applies "Reduced Inequalities" to sentences that clearly belong to other goals (Clean Water, Gender Equality, Decent Work). This is a known limitation of zero shot classification on broad, semantically overlapping categories.
 
<img width="1032" height="594" alt="Screenshot 2026-06-27 at 11 06 35 AM" src="https://github.com/user-attachments/assets/dd903ee9-8383-4d89-a149-5ce6df447048" />

<img width="1005" height="599" alt="Screenshot 2026-06-27 at 11 12 17 AM" src="https://github.com/user-attachments/assets/3ffa810a-f0d0-4efc-878e-e7a4b4fc2978" />

<img width="990" height="595" alt="Screenshot 2026-06-27 at 11 13 33 AM" src="https://github.com/user-attachments/assets/e21a27d7-7664-46ec-b63a-79e4c027bc03" />

<img width="996" height="590" alt="Screenshot 2026-06-27 at 11 14 10 AM" src="https://github.com/user-attachments/assets/374c516a-b9d8-4cc2-9691-265dc3ebcaaa" />


 
## Approach
 
The analysis follows the following pipeline:
 
1. **Text extraction.** PDFs opened with `pdfplumber`. Raw text cleaned of hyphenation artifacts and whitespace. Sentences tokenized with NLTK's `sent_tokenize`. Sentences under 8 words filtered as noise.
2. **Sentiment and urgency scoring.** VADER `SentimentIntensityAnalyzer` applied to each sentence. Urgency defined as `−compound score` so that dire/negative language produces a positive urgency value. Rolling average plotted across the full document to identify structural sentiment shifts.
3. **SDG classification.** `facebook/bart-large-mnli` zero-shot classifier assigns each sentence to one or more of the 17 SDG labels (multi label, threshold = 0.5). Sentences below threshold on all labels marked "Unclassified." Manual check of 20 randomly sampled sentences with slef assigned labels compared to classifier output.
4. **Cross year comparison and correlation.** `process_report()` function applies the full pipeline to each PDF and returns a tidy DataFrame with `sentence`, `sdg`, `urgency`, and `year`. Results concatenated into `df_master`. Urgency scores merged with official UN progress ratings (sourced from UN progress charts) and correlated via Spearman rank correlation.
## Classifier accuracy assessment
 
<img width="992" height="661" alt="Screenshot 2026-06-27 at 11 17 12 AM" src="https://github.com/user-attachments/assets/71c993a3-f66c-46d9-aa29-b3b8550bbcfa" />

 
Manual evaluation showed the classifier performs well on sentences with explicit, specific topic language. It struggles with:
- Sentences describing data visualizations or graphics
- Short sentences lacking topical context
- Sentences spanning multiple SDGs simultaneously
The Reduced Inequalities overclassification rate was the most notable systematic error, driven by the label's broad semantic coverage across water, food, gender, and economic inequality.
 
## Tools and stack
 
- **Python:** pandas, numpy, pdfplumber, nltk (VADER), scikit-learn, scipy, matplotlib, seaborn
- **NLP model:** `facebook/bart-large-mnli` via HuggingFace Transformers (zero shot classification)
- **Clustering:** scipy hierarchical clustering (Ward linkage), TF-IDF vectorization
- **Visualization:** matplotlib, seaborn
- **Data sources:** UN SDG Reports 2016, 2021, 2025 (unstats.un.org); UN SDG Progress Charts 2021, 2025
## Limitations
 
- **Zero shot classifier bias.** The model over assigns "Reduced Inequalities" due to the label's semantic breadth. This may inflate its apparent prevalence across all three reports.
- **VADER is a lexical model.** It does not understand negation, irony, or domain specific framing. UN reports use technical language that VADER was not trained on.
- **No 2016 progress chart.** The 2016 inaugural report predates the UN's standardized progress rating format. The cross year correlation is therefore limited to 2021 and 2025 data points.
- **PDF extraction artifacts.** The columns in the PDF layout cause some sentences to be extracted in garbled order, mixing content from adjacent columns. The 8 word minimum filter removes most but not all of this noise.
## What I'd do with more time
 
- Add more report years (2018, 2020, 2022, 2023) to build a richer urgency time series per goal.
- Fine tune a classifier on labeled SDG sentence data to reduce the Reduced Inequalities overclassification bias.
- Cross reference urgency scores against quantitative indicator data (e.g., poverty headcount, CO^2 emissions) rather than qualitative progress ratings.
- Build a goal level urgency forecasting model to predict how 2026–2030 reports are likely to read as the deadline approaches.
