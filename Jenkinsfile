pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
metadata:
  namespace: jenkins
spec:
  serviceAccountName: jenkins
  containers:
  - name: kaniko-ui
    image: gcr.io/kaniko-project/executor:debug
    command: ["sleep"]
    args: ["9999999"]
    env:
    - name: AWS_REGION
      value: "eu-west-1"
  - name: kaniko-cart
    image: gcr.io/kaniko-project/executor:debug
    command: ["sleep"]
    args: ["9999999"]
    env:
    - name: AWS_REGION
      value: "eu-west-1"
  - name: kaniko-catalog
    image: gcr.io/kaniko-project/executor:debug
    command: ["sleep"]
    args: ["9999999"]
    env:
    - name: AWS_REGION
      value: "eu-west-1"
  - name: kaniko-orders
    image: gcr.io/kaniko-project/executor:debug
    command: ["sleep"]
    args: ["9999999"]
    env:
    - name: AWS_REGION
      value: "eu-west-1"
  - name: kubectl
    image: alpine/k8s:1.31.4
    command: ["sleep"]
    args: ["9999999"]
"""
        }
    }
    environment {
        REGISTRY = '256532498008.dkr.ecr.eu-west-1.amazonaws.com'
        TAG = "${BUILD_NUMBER}"
    }
    stages {
        stage('Build UI') {
            steps {
                container('kaniko-ui') {
                    sh '/kaniko/executor --context=${WORKSPACE}/src/ui --destination=${REGISTRY}/retail-store-sample-ui:${TAG} --destination=${REGISTRY}/retail-store-sample-ui:latest'
                }
            }
        }
        stage('Build Cart') {
            steps {
                container('kaniko-cart') {
                    sh '/kaniko/executor --context=${WORKSPACE}/src/cart --destination=${REGISTRY}/retail-store-sample-cart:${TAG} --destination=${REGISTRY}/retail-store-sample-cart:latest'
                }
            }
        }
        stage('Build Catalog') {
            steps {
                container('kaniko-catalog') {
                    sh '/kaniko/executor --context=${WORKSPACE}/src/catalog --destination=${REGISTRY}/retail-store-sample-catalog:${TAG} --destination=${REGISTRY}/retail-store-sample-catalog:latest'
                }
            }
        }
        stage('Build Orders') {
            steps {
                container('kaniko-orders') {
                    sh '/kaniko/executor --context=${WORKSPACE}/src/orders --destination=${REGISTRY}/retail-store-sample-orders:${TAG} --destination=${REGISTRY}/retail-store-sample-orders:latest'
                }
            }
        }
        stage('Deploy to EKS') {
            steps {
                container('kubectl') {
                    sh """
                        kubectl set image deployment/ui ui=${REGISTRY}/retail-store-sample-ui:${TAG} -n default
                        kubectl set image deployment/carts carts=${REGISTRY}/retail-store-sample-cart:${TAG} -n default
                        kubectl set image deployment/catalog catalog=${REGISTRY}/retail-store-sample-catalog:${TAG} -n default
                        kubectl set image deployment/orders orders=${REGISTRY}/retail-store-sample-orders:${TAG} -n default
                        kubectl rollout status deployment/ui -n default --timeout=120s
                    """
                }
            }
        }
    }
}
