This is how the current table UpisGodine looks like: 

UpisGodine,1,2020-09-30,1,0,,1,1,
UpisGodine,2,2021-10-01,2,30,,2,2,
UpisGodine,3,2022-10-02,3,60,,3,3,
UpisGodine,4,2023-10-03,4,90,,4,4,
UpisGodine,5,2024-10-04,4,120,,5,5,
UpisGodine,6,2025-10-05,4,150,,6,6,
UpisGodine,7,2020-10-06,1,0,,1,7,
UpisGodine,8,2021-10-07,2,30,,2,8,
UpisGodine,9,2022-10-08,3,60,,3,9,
UpisGodine,10,2023-10-09,4,90,,4,10,
UpisGodine,11,2024-10-10,4,120,,5,11,
UpisGodine,12,2025-10-11,4,150,,6,12,
UpisGodine,13,2020-10-12,1,0,,1,13,
UpisGodine,14,2021-10-13,2,30,,2,14,
UpisGodine,15,2022-10-14,3,60,,3,15,
ObnovaGodine,16,2021-09-30,1,0,Nedovoljno ESPB bodova,2,1,1
ObnovaGodine,17,2022-10-01,2,30,Nedovoljno ESPB bodova,3,2,2
ObnovaGodine,18,2023-10-02,3,60,Nedovoljno ESPB bodova,4,3,3
ObnovaGodine,19,2025-11-20,2,30,Neuspešan upis - nedovoljno položenih ispita,5,3,3
ObnovaGodine,20,2025-11-20,1,30,Neuspešan upis - nedovoljno položenih ispita,5,3,3
ObnovaGodine,21,2025-11-20,2,1,Neuspešan upis - nedovoljno položenih ispita,5,1,3
ObnovaGodine,22,2025-11-20,3,1,Neuspešan upis - nedovoljno položenih ispita,5,1,3


And the API call to create a new ObnovaGodina:

{
  "studentIndeksId": 1,
  "godina": 3,
  "skolskaGodinaId": 5,
  "prethodniUpisId": 3,
  "nextYearDrziPredmetIds": [],
  "prenetiESPB": 1,
  "razlogObnove": "Neuspešan upis - nedovoljno položenih ispita"
}

This student 1 shouldnt be able to get make ObnovaGodine for 3rd year, because he is in year 2