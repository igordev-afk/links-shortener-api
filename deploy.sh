#!/bin/bash

minikube start --vm-driver=docker

if [ $? -ne 0 ]; then
    echo "Error occurred with starting Minikube"
    exit 1
fi

# Применение конфигураций Kubernetes

apply_configuration() {
    kubectl apply -f "$1"
    if [ $? -ne 0 ]; then
        echo "Error occurred with applying configuration: $1"
        exit 1
    fi
}

# Применение конфигураций для сервисов
apply_configuration "api-handler-service/k8s/api-handler-service-deployment.yaml"
apply_configuration "api-handler-service/k8s/api-handler-service-service.yaml"
apply_configuration "auth-service/k8s/auth-service-deployment.yaml"
apply_configuration "auth-service/k8s/auth-service-service.yaml"
apply_configuration "gateway-service/k8s/gateway-service-deployment.yaml"
apply_configuration "gateway-service/k8s/gateway-service-service.yaml"
apply_configuration "shortlink-service/k8s/shortlink-service-deployment.yaml"
apply_configuration "shortlink-service/k8s/shortlink-service-service.yaml"

# Применение общих конфигураций
apply_configuration "kubernetes_intro/config-map.yaml"
apply_configuration "kubernetes_intro/postgres.yaml"
apply_configuration "kubernetes_intro/redis.yaml"

echo "Все конфигурации успешно применены"

minikube dashboard

