#!/bin/sh
# Require jq to be installed

export TOKEN=$(curl -X POST "https://sso.aeris-data.fr/auth/realms/aeris/protocol/openid-connect/token" --insecure \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=USER_EMAIL" \
 -d "password=USER_PASSWORD" \
 -d 'grant_type=password' \
 -d "client_id=aeris-public" | jq -r '.access_token')
HEADER="--header=Authorization: Bearer $TOKEN"
echo $HEADER

wget "$HEADER" -qO- https://api.sedoo.fr/COMPLETE_URL_OF_THE_REQUESTED_SERVICE

