from flask import Flask, request, jsonify
from flask_cors import CORS

import joblib


# =========================================================
# FLASK APPLICATION
# =========================================================

app = Flask(__name__)

CORS(app)


# =========================================================
# LOAD TRAINED MODEL
# =========================================================

model = joblib.load(
    "phishing_model.pkl"
)


# =========================================================
# HEALTH CHECK
# =========================================================

@app.route("/health", methods=["GET"])
def health():

    return jsonify({
        "status": "ML Service is running",
        "model": "Phishing Detection Model"
    })


# =========================================================
# PREDICTION API
# =========================================================

@app.route("/predict", methods=["POST"])
def predict():

    data = request.get_json()

    if not data:

        return jsonify({
            "error": "Request body is required"
        }), 400


    email = data.get(
        "email",
        ""
    )


    if not email.strip():

        return jsonify({
            "error": "Email content is required"
        }), 400


    # -----------------------------------------------------
    # MODEL PREDICTION
    # -----------------------------------------------------

    prediction = model.predict([email])[0]


    probabilities = model.predict_proba(
        [email]
    )[0]


    phishing_probability = (
        probabilities[1] * 100
    )


    legitimate_probability = (
        probabilities[0] * 100
    )


    # -----------------------------------------------------
    # RESULT
    # -----------------------------------------------------

    if prediction == 1:

        result = "PHISHING"

    else:

        result = "LEGITIMATE"


    # -----------------------------------------------------
    # RESPONSE
    # -----------------------------------------------------

    return jsonify({

        "prediction": result,

        "phishingScore": round(
            phishing_probability,
            2
        ),

        "legitimateScore": round(
            legitimate_probability,
            2
        )
    })


# =========================================================
# RUN APPLICATION
# =========================================================

if __name__ == "__main__":

    app.run(
        host="0.0.0.0",
        port=5000,
        debug=True
    )