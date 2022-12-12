import UIKit
import Flutter
import AliceOnboarding

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    var controller: FlutterViewController? = nil
    var flutterResult: FlutterResult? = nil

  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
      controller = window?.rootViewController as? FlutterViewController
      let batteryChannel = FlutterMethodChannel(name: "com.alicebiometrics/onboarding",
                                                binaryMessenger: controller!.binaryMessenger)

      batteryChannel.setMethodCallHandler({
        (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
          if let args = call.arguments as? Dictionary<String, Any>,
             let email = args["email"] as? String,
             let trialToken = args["trialToken"] as? String {
              let userInfo = UserInfo(email: email)
              let authenticator = TrialAuthenticator(trialToken: trialToken, userInfo: userInfo)
              authenticator.execute { authResult in
                  switch authResult {
                  case .success(var userToken):
                      var config = OnboardingConfig()
                      config = config.withUserToken(userToken)
                      config = try! config.withAddSelfieStage()
                      config = try! config.withAddDocumentStage(ofType: DocumentType.idcard)

                      let onboarding = Onboarding(self.controller!, config: config)
                      onboarding.run { onboardingResult in
                          switch onboardingResult {
                          case let .success(userStatus):
                              result(userStatus["user_id"])
                          case let .failure(error):
                              result(FlutterError(code: "error", message: error.description, details: error.description))
                          case .cancel:
                               result("cancel")
                          case .dismissButton:
                              result("dismissButton")
                          @unknown default:
                              result("unknownError")
                          }
                      }
                  case .failure(_):
                      break
                  }
              }
           } else {
             result(FlutterError.init(code: "bad args", message: nil, details: nil))
           }
      })
      
      GeneratedPluginRegistrant.register(with: self)

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
    
    func authenticator(trialToken: String, email: String) {
    
    }

    
}
