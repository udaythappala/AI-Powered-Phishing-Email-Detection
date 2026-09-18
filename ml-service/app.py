from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib

app = Flask(__name__)
CORS(app)

# Load trained ML model
model = joblib.load("phishing_model.pkl")


@app.route("/predict", methods=["POST"])
def predict():

    data = request.get_json()

    email = data.get("email", "")

    if not email:
        return jsonify({
            "error": "Email content is required"
        }), 400

    # Prediction
    prediction = model.predict([email])[0]

    # Probability
    probabilities = model.predict_proba([email])[0]

    phishing_probability = probabilities[1] * 100

    if prediction == 1:
        result = "PHISHING"
    else:
        result = "LEGITIMATE"

    return jsonify({
        "prediction": result,
        "phishingScore": round(phishing_probability, 2)
    })


@app.route("/health", methods=["GET"])
def health():

    return jsonify({
        "status": "ML Service is running"
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)