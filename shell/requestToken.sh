export TOKEN=$(curl -X POST "https://sso.aeris-data.fr/auth/realms/aeris/protocol/openid-connect/token" --insecure \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=USER_EMAIL" \
 -d "password=USER_PASSWORD" \
 -d 'grant_type=password' \
 -d "client_id=aeris-public" | jq -r '.access_token')
TOKEN="Bearer: $TOKEN"
echo $TOKEN
