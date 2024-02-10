# Links Shortener API

Welcome to the Links Shortener API documentation! This API service provides the functionality to shorten URLs and retrieve the original URL from the shortened one.

## Overview

The Links Shortener API offers a simple yet effective solution for shortening URLs, making them more manageable and easier to share. It employs Bloom Filter technology for efficient hashing and utilizes a JSON file as storage.

## How It Works

1. **Sign Up**: Register to obtain authentication credentials. Upon successful registration, you will receive a JWT token, which will be used for subsequent API requests. Tokens are valid for one hour.

2. **Shorten URL**: Send a POST request to `/shorten_url` with the original URL (`"longUrl"`) in the request body. This endpoint will return a shortened URL (`"shortUrl"`).

3. **Access Shortened URL**: Use the shortened URL obtained in step 2. Whenever this URL is accessed, the service will automatically redirect the user to the original URL.

4. **Token Management**: Tokens expire after one hour. Use the `/signin` endpoint to obtain a new token by providing your email and password. This endpoint returns a refreshed JWT token, extending your session for another hour.

## Getting Started

### Signing Up
To register and obtain your authentication token, send a POST request to `/signup` with the following JSON payload:

```json
{
  "firstName": "YourFirstName",
  "lastName": "YourLastName",
  "email": "your.email@example.com",
  "password": "YourSecurePassword"
}
```

### Shortening URL
Send a POST request to /shorten_url with the following JSON payload and request header with valid Bearer token:

```json
{
  "longUrl": "https://example.com/your/long/url"
}
```

### Refreshing Token
To refresh your authentication token, send a POST request to /signin with your email and password:

```json
{
  "email": "your.email@example.com",
  "password": "YourSecurePassword"
}
```

## Deployment in Minikube

To deploy the Links Shortener API in Minikube, follow these steps:

1. **Start Minikube**: Ensure Minikube is installed and running. If not, you can install it following the [Minikube documentation](https://minikube.sigs.k8s.io/docs/start/).

2. **Apply Kubernetes Configurations**: Navigate to the Kubernetes directory of the project and apply the configurations using the following command for each YAML file:
   
```bash
kubectl apply -f <filename>
```

This command will apply the configurations necessary for deploying the API to your Minikube cluster.

3. **Access API**: Once deployed, you can access the API through the exposed service. Use the Minikube IP and the assigned port to access the API endpoints.

To find the IP address of your Minikube cluster, you can use the following command:

```bash
minikube ip
```

Then, you can access the API using the obtained IP address and the assigned port.

Replace `<minikube-ip>` with your Minikube IP address and `<assigned-port>` with the port assigned to the 'gateway-service'.
'http://'<minikube-ip>':'<assigned-port>'/shorten_url'

This will allow you to deploy and access the Links Shortener API within your Minikube environment.

## Security
Ensure the security of your JWT token to prevent unauthorized access to your account and data. 
