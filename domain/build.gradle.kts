plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // Kotlin 표준 라이브러리
    implementation(libs.kotlin.stdlib)

    // 코루틴 - 안드로이드 의존성 없는 순수 코루틴
    implementation(libs.kotlin.coroutines.core)

    // 테스트
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)
}