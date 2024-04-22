import logo from './logo.svg';
import './App.css';
import CreateAccountForm from "./CreateAccountForm";
import Accounts from "./Accounts";

function App() {


  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <h1>Create Account</h1>
        <div>
          <a
              href="http://localhost:8083/login"
              className="btn btn-lg btn-primary"
          >
            <i className="fab fa-google"></i> Continue with Google or Facebook <i className="fab fa-facebook"></i>
          </a>
        </div>
        <Accounts />

        <CreateAccountForm />


        <p>

          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
