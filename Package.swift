// swift-tools-version:5.3
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let version = "2.3.12"
let checksum = "ec99bd96d9ab6ff9bc60736d4e76d8135f9f8b4dd58baddaafa0d087fdf3dff1"

let package = Package(
    name: "AliceOnboarding",
    platforms: [
        .iOS(.v9)
    ],
    products: [
        .library(
            name: "AliceOnboarding",
            targets: ["AliceOnboardingBundle"]),
    ],
    targets: [
        .target(
            name: "AliceOnboardingBundle",
            dependencies: ["AliceOnboarding"]
        ),
        .binaryTarget(
            name: "AliceOnboarding",
            url: "https://storage.googleapis.com/alicebiometrics.com/releases/ios/AliceOnboarding-2.3.12.zip",
            checksum: "\(checksum)"
        ),
    ]
)
