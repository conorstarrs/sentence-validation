# Conor Starrs - Sentence Validation Assignment

This project contains the source code for a serverless application deployed to Amazon Web Services. It includes the following files and folders.

- SentenceValidationFunction/src/main - Code for the application's Lambda function, written in Java.
- SentenceValidationFunction/src/test - Unit tests for the application's Lambda function code. 
- template.yaml - A template that defines the application's AWS resources (infrastructure as code). This gets interpreted and automatically deployed to AWS through CloudFormation with SAM commands. It will deploy an API Gateway API, a Lambda function, an S3 bucket (for static UI files) and a CloudFront distribution.

## Live Demo

Please visit https://assignment.conorstarrs.dev/ to view a live demo of the application.

## Run the unit tests

If you would like to run the unit tests, please download the source code from git, then run:

```bash
mvn test
```

For example:

![alt text](https://assignment.conorstarrs.dev/images/unit-tests.png)
