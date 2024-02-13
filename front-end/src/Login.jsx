export default function Login() {
    return (<>
    <div className="center">
      <h1>Login</h1>
      <form autoComplete="off">
        <div className="txt_field">
          <input type="text" autoComplete="off" />
          <span></span>
          <label>Username</label>
        </div>
        <div className="txt_field">
          <input type="password" />
          <span></span>
          <label>Password</label>
        </div>
        <div className="pass">Forgot Password?</div>
        <a href="http://localhost:8080/" className="login-btn"> Login with Google</a>
        <div className="signup_link">
          Not a member? <a href="#">Signup</a>
        </div>
      </form>
    </div>
    </>)
}