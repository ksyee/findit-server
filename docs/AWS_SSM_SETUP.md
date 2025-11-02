# AWS Systems Manager Parameter Store 설정 가이드

## 1. Parameter Store에 파라미터 생성

### AWS Console 사용

**AWS Console → Systems Manager → Parameter Store → Create parameter**

다음 파라미터들을 생성하세요:

| Name | Type | Value |
|------|------|-------|
| `/findit/prod/datasource-url` | String | `jdbc:postgresql://findit-postgres.cxksmc26k7v8.ap-northeast-2.rds.amazonaws.com:5432/findit` |
| `/findit/prod/datasource-username` | String | `postgres` |
| `/findit/prod/datasource-password` | **SecureString** | `YOUR_PASSWORD` |
| `/findit/prod/api-security-key` | **SecureString** | `YOUR_API_KEY` |
| `/findit/prod/api-security-enabled` | String | `true` |
| `/findit/prod/police-api-enabled` | String | `true` |

### AWS CLI 사용

```bash
aws ssm put-parameter \
  --name "/findit/prod/datasource-url" \
  --type "String" \
  --value "jdbc:postgresql://findit-postgres.cxksmc26k7v8.ap-northeast-2.rds.amazonaws.com:5432/findit" \
  --region ap-northeast-2

aws ssm put-parameter \
  --name "/findit/prod/datasource-username" \
  --type "String" \
  --value "postgres" \
  --region ap-northeast-2

aws ssm put-parameter \
  --name "/findit/prod/datasource-password" \
  --type "SecureString" \
  --value "YOUR_PASSWORD" \
  --region ap-northeast-2

aws ssm put-parameter \
  --name "/findit/prod/api-security-key" \
  --type "SecureString" \
  --value "YOUR_API_KEY" \
  --region ap-northeast-2

aws ssm put-parameter \
  --name "/findit/prod/api-security-enabled" \
  --type "String" \
  --value "true" \
  --region ap-northeast-2

aws ssm put-parameter \
  --name "/findit/prod/police-api-enabled" \
  --type "String" \
  --value "true" \
  --region ap-northeast-2

> 필요 시 일시적으로 데이터를 중지하려면 `police-api-enabled` 값을 `false`로 덮어써 비활성화할 수 있습니다.
```

## 2. EC2 IAM Role 설정

### IAM Role 생성

1. **IAM Console → Roles → Create role**
2. **Trusted entity type: AWS service**
3. **Use case: EC2**
4. **Permissions policies:**
   - `AmazonSSMManagedInstanceCore`
   - 또는 커스텀 정책:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ssm:GetParameter",
        "ssm:GetParameters",
        "ssm:GetParametersByPath"
      ],
      "Resource": "arn:aws:ssm:ap-northeast-2:*:parameter/findit/*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "kms:Decrypt"
      ],
      "Resource": "*"
    }
  ]
}
```

5. **Role name: `findit-ec2-role`**
6. **Create role**

### EC2에 IAM Role 연결

1. **EC2 → Instances → 인스턴스 선택**
2. **Actions → Security → Modify IAM role**
3. **`findit-ec2-role` 선택**
4. **Update IAM role**

## 3. EC2에서 .env 파일 삭제

```bash
# EC2 SSH에서 실행
sudo rm -f /opt/findit/.env
```

## 4. 애플리케이션 실행

이제 애플리케이션은 환경 변수 없이 실행되며, AWS SSM에서 자동으로 설정을 가져옵니다:

```bash
docker run -d \
  --name findit \
  --restart unless-stopped \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  ghcr.io/ksyee/findit-server:latest
```

## 5. 검증

```bash
# 애플리케이션 로그에서 SSM 로딩 확인
docker logs findit | grep "AWS SSM"

# 예상 출력:
# Loaded 6 parameters from AWS SSM path '/findit/prod'.
```

## 파라미터 매핑

| SSM Parameter | Spring Property |
|--------------|-----------------|
| `/findit/prod/datasource-url` | `spring.datasource.url` |
| `/findit/prod/datasource-username` | `spring.datasource.username` |
| `/findit/prod/datasource-password` | `spring.datasource.password` |
| `/findit/prod/api-security-enabled` | `security.api.enabled` |
| `/findit/prod/api-security-key` | `security.api.key` |
| `/findit/prod/police-api-enabled` | `police.api.enabled` |

## 장점

- ✅ 소스 코드에 민감한 정보 없음
- ✅ .env 파일 관리 불필요
- ✅ 암호화된 파라미터 지원 (SecureString)
- ✅ 중앙 집중식 설정 관리
- ✅ IAM 정책으로 접근 제어
- ✅ 파라미터 버전 관리 지원
- ✅ 감사 로그 (CloudTrail)
