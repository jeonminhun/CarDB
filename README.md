# 🚗 CarDB - 자동차 관리 앱 (Android)

> 부모님의 요청으로 제작한 차량 관리 앱  
> 차량 정보, 점검 기록, 사진 등을 손쉽게 등록하고 관리할 수 있는 Android 애플리케이션

![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![RoomDB](https://img.shields.io/badge/Room%20DB-4285F4?logo=sqlite)
![Material Design](https://img.shields.io/badge/Material%20Design-757575?logo=materialdesign)
![Glide](https://img.shields.io/badge/Glide-FFCC00)

---

## 📖 프로젝트 개요

- **프로젝트명**: CarDB  
- **기간**: 2025.04.20 ~ 2025.04.25  
- **유형**: 개인 프로젝트  
- **GitHub**: [https://github.com/jeonminhun/CarDB](https://github.com/jeonminhun/CarDB)

---

## 🎯 프로젝트 목적

부모님의 요청으로, 업무용 차량의 **정보 및 점검 내역을 손쉽게 관리할 수 있는 앱**을 개발했습니다.  
단순한 기록이 아닌, **사진·날짜·특이사항을 함께 관리**할 수 있어 실사용 중심으로 설계되었습니다.

---

## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| **언어** | Java |
| **개발 환경** | Android Studio |
| **데이터베이스** | Room DB |
| **이미지 처리** | Glide |
| **UI 구성** | ViewPager2, Material Design |
| **저장 구조** | 내부 저장소 + Room Entity 구조 |

---

## 🌟 주요 기능

### 📸 사진 관리
- **ViewPager2** 기반 사진 슬라이더  
- 사진 촬영 및 갤러리에서 선택 가능  
- 썸네일 클릭 시 대표 이미지 변경  

### 🧾 차량 정보 관리
- 차량 **차종 / 차량번호 / 날짜 / 특이사항** 입력 및 수정  
- Room DB 기반 **로컬 데이터 영구 저장**  
- 상세 페이지에서 차량별 사진 및 정보 조회 가능  

### 💡 UX/UI
- **Material Design** 적용으로 일관된 디자인  
- ViewPager2 + RecyclerView를 활용한 부드러운 스크롤  
- 반응형 버튼 및 입력 필드 구성  

---

## 🧪 트러블슈팅

### 1. 카메라 권한 문제
- **문제**: 사진 촬영 시 `Permission Denial` 오류 발생  
- **원인**: `AndroidManifest.xml` 내 CAMERA 권한 누락  
- **해결**: 런타임 퍼미션 요청 및 `FileProvider` 설정 추가  
- **결과**: 카메라 촬영 및 갤러리 이미지 등록 정상 작동  

---

### 2. 이미지 경로 저장 비동기 처리 문제
- **문제**: 이미지 경로를 DB에 저장 시 비동기 처리로 인한 오류 발생  
- **원인**: Room DB와 비동기 이미지 저장 타이밍 불일치  
- **해결**: `Handler`를 사용하여 UI 스레드 내에서 DB 작업 수행  
- **결과**: 이미지 저장 및 불러오기 안정화  

---


## 👨‍💻 개발자 정보

- **이름**: 전민훈 (Minhoon Jeon)  
- **GitHub**: [@jeonminhun](https://github.com/jeonminhun)  
- **이메일**: jeonminhoon.dev@gmail.com  

---

## 📄 라이선스

이 프로젝트는 **MIT License** 하에 배포됩니다.
