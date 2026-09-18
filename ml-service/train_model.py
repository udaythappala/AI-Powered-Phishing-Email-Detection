import pandas as pd
import joblib

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline


# =========================================================
# TRAINING DATA
# =========================================================

data = {
    "email": [

        # ---------------- PHISHING ----------------

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

        "Your bank account will be closed unless you verify your information",

        "Click the link below to confirm your password",

        "Urgent security notice login to verify your account",

        "Your payment failed. Update your billing information immediately",

        "Verify your identity to avoid account suspension",

        "You have an important message. Click here to read it",

        "Confirm your banking credentials immediately",

        "Your account has suspicious activity. Login now",

        "Reset your password by clicking the link below",

        "Your refund is waiting. Provide your bank details",

        # ---------------- LEGITIMATE ----------------

        "Meeting is scheduled for tomorrow at 10 AM",

        "Please find the project report attached",

        "Your Amazon order has been shipped",

        "Here are the notes from today's meeting",

        "Please review the assignment before Friday",

        "The team meeting has been moved to Monday",

        "Your monthly electricity bill is attached",

        "Thank you for submitting the project",

        "The interview is scheduled for next week",

        "Please find the invoice attached",

        "The project discussion is scheduled for Monday",

        "Please review the document and share your feedback",

        "Your course registration has been confirmed",

        "The team presentation is scheduled for tomorrow",

        "Attached is the monthly financial report",

        "Thank you for attending the meeting",

        "The assignment submission deadline is Friday",

        "Your order has been delivered successfully",

        "Please find the requested information attached",

        "The workshop will begin at 9 AM tomorrow"
    ],

    "label": [

        # Phishing = 1
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,

        # Legitimate = 0
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0,
        0, 0, 0, 0, 0
    ]
}


# =========================================================
# CREATE DATAFRAME
# =========================================================

df = pd.DataFrame(data)

print("Training samples:", len(df))

print(
    "Phishing emails:",
    sum(df["label"] == 1)
)

print(
    "Legitimate emails:",
    sum(df["label"] == 0)
)


# =========================================================
# MACHINE LEARNING PIPELINE
# =========================================================

model = Pipeline([

    (
        "tfidf",
        TfidfVectorizer(
            lowercase=True,
            stop_words="english",
            ngram_range=(1, 2),
            max_features=5000
        )
    ),

    (
        "classifier",
        LogisticRegression(
            max_iter=1000
        )
    )
])


# =========================================================
# TRAIN MODEL
# =========================================================

model.fit(
    df["email"],
    df["label"]
)


# =========================================================
# SAVE MODEL
# =========================================================

joblib.dump(
    model,
    "phishing_model.pkl"
)


print()
print("========================================")
print("Model trained successfully!")
print("Model saved as phishing_model.pkl")
print("========================================")