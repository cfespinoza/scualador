import json
import os
import pandas as pd

if __name__ == "__main__":
    path = os.environ.get("NOTAS_DIR")
    scores_files = os.listdir(path)
    scores = []
    for sf in scores_files:
        alumno = sf.replace(".json", "")
        score = {"alumno": alumno}
        with open(os.path.join(path, sf), 'r') as f:
            score.update(json.load(f))
            scores.append(score)
    scores_pd = pd.DataFrame(scores)
    scores_pd.to_csv(f"{path}/summary.csv", index=False)