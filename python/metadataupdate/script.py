# Il est nécessaire d'importer keycloak-client: sudo pip3 install keycloak-client
import json
import requests
from keycloak import KeycloakOpenID

# Permet de recuperer un token d'identification pour pouvoir pouvoir utiliser les services REST du Catalogue
# Il faut renseigner USER_EMAIL et USER_PASSWORD du compte sur le keycloak AERIS (le mot de passe doit être créé si vous avez toujours utilisé la connexion via renater ou orcid)
# adresse de gestion du compte keycloak : https://sso.aeris-data.fr/auth/realms/aeris/account
def getToken():
    keycloak_openid = KeycloakOpenID(server_url="https://sso.aeris-data.fr/auth/",
                    client_id="aeris-public",
                    realm_name="aeris",
                    verify=True)
    token = keycloak_openid.token("USER_EMAIL", "USER_PASSWORD")
    return token

# Permet de creer ou mettre a jour une fiche de métadonnées
# Fait appel a un service REST du catalogue avec comme argument l'id de la fiche et le path du fichier json de la fiche
# champ id obligatoire

#L'URL du service de métadonnées est indiquée sur le site du SEDOO
URL_SERVICE = 'URL_SERVICE_METADONNEES'

def getHeader():
    return {'Authorization': 'Bearer ' + getToken()['access_token'], 'Accept': 'application/json'}

def loadData(jsonPath):
    with open(jsonPath, 'r') as myfile:
        data=myfile.read()
    return json.loads(data)
  
def patchMetadata(jsonPath):
    metadata = loadData(jsonPath)
    response = requests.patch(URL_SERVICE + metadata['id'], json=metadata, headers=getHeader())
    print(response)
    print(response.content)  
        
if __name__ == '__main__':
    patchMetadata("./patch.json")
