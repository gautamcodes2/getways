#!/bin/sh

# Create secret folder if it doesn't exist
mkdir -p /webhook-secret

# Start fresh webhook listener, capture secret
stripe listen --forward-to  https://4dadbe8d36a7.ngrok-free.app/api/payment/webhook 2>&1 | \
tee /dev/stderr | \
grep -o 'whsec_[a-zA-Z0-9]*' | \
head -1 > /webhook-secret/secret

# Optionally, update .env with webhook secret
SECRET=$(cat /webhook-secret/secret)
if [ -n "$SECRET" ]; then
  if grep -q '^STRIPE_WEBHOOK_SECRET=' /app/.env; then
    sed -i "s|^STRIPE_WEBHOOK_SECRET=.*|STRIPE_WEBHOOK_SECRET=$SECRET|" /app/.env
  else
    echo STRIPE_WEBHOOK_SECRET=$SECRET >> /app/.env
  fi
fi

# Keep logs visible
tail -f /tmp/stripe.log
