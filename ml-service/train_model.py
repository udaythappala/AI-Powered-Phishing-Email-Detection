import pandas as pd
import joblib

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline


# Sample phishing and legitimate email data
data = {
    "email": [
        "Urgent! Verify your account immediately by clicking this link",
        "Your account has been suspended. Click here to restore access",
        "Congratulations! You won a prize. Claim your reward now",
        "Your password will expire today. Verify your password",
        "We detected unusual activity. Login immediately to secure your account",
        "You have received a payment. Confirm your bank details",
        "Click here to update your banking information",
        "Your account requires immediate verification",
        "Please submit your credit card details to receive the refund",
        "Security alert! Confirm your identity now",

        "Meeting is scheduled for tomorrow at 10 AM",
        "Please find the project report attached",
        "Your Amazon order has been shipped",
        "Here are the notes from today's meeting",
        "Please review the assignment before Friday",
        "The team meeting has been moved to Monday",
        "Your monthly electricity bill is attached",
        "Thank you for submitting the project",
        "The interview is scheduled for next week",
        "Please find the invoice attached"
    ],

    "label": [
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,

        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0
    ]
}

df = pd.DataFrame(data)


# TF-IDF + Logistic Regression
model = Pipeline([
    ("tfidf", TfidfVectorizer(
        lowercase=True,
        stop_words="english",
        ngram_range=(1, 2)
    )),
    ("classifier", LogisticRegression())
])


# Train model
model.fit(df["email"], df["label"])


# Save trained model
joblib.dump(model, "phishing_model.pkl")

print("Model trained successfully!")
print("Model saved as phishing_model.pkl")