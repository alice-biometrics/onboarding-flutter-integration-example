// swift-tools-version:5.3
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let version = "2.3.13"
let checksum = "287f8b9ace6e0a35afe88bc2d0cd1a86b19de90f313c280d7b7dae7344b6dc29"

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
            url: "https://storage.googleapis.com/alicebiometrics.com/releases/ios/AliceOnboarding-\(version).zip",
            checksum: "\(checksum)"
        ),
    ]
)
