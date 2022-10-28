import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper
import com.okta.idx.sdk.api.model.UserProfile
import com.okta.idx.sdk.api.model.VerifyAuthenticatorOptions

fun main() {
    val idxWrapper = IDXAuthenticationWrapper()

    // begin transaction
    var rsp = idxWrapper.begin()

    // get proceed context
    var ctx = rsp.proceedContext

    // enroll user
    rsp = idxWrapper.fetchSignUpFormValues(ctx)

    print("first name: ")
    val fName = readln()
    print("last name: ")
    val lName = readln()
    print("email: ")
    val email = readln()

    // set user profile
    val userProfile = UserProfile()
    userProfile.addAttribute("lastName", lName);
    userProfile.addAttribute("firstName", fName);
    userProfile.addAttribute("email", email);

    ctx = rsp.proceedContext

    // register user with proceed context context
    rsp = idxWrapper.register(ctx, userProfile)
    ctx = rsp.proceedContext
    val authenticator = rsp.authenticators[0]

    rsp = idxWrapper.selectAuthenticator(ctx, authenticator);
    ctx = rsp.proceedContext

    print("password: ")
    val pwd = readln()
    var options = VerifyAuthenticatorOptions(pwd)
    rsp = idxWrapper.verifyAuthenticator(ctx, options)
    ctx = rsp.proceedContext

    rsp = idxWrapper.selectAuthenticator(ctx, rsp.authenticators[0])
    ctx = rsp.proceedContext

    print("code: ")
    val code = readln()
    options = VerifyAuthenticatorOptions(code)
    rsp = idxWrapper.verifyAuthenticator(ctx, options)
    ctx = rsp.proceedContext

    rsp = idxWrapper.skipAuthenticatorEnrollment(ctx)
    println("access_token: ${rsp.tokenResponse.accessToken}\nid_token: ${rsp.tokenResponse.idToken}")
}