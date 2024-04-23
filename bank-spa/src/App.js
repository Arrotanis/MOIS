import { AppBar, Toolbar, Typography, Container, Button, Grid } from '@mui/material';
import Accounts from "./Accounts";
import React, { useState } from 'react';

function App() {
    const [loggedIn, setLoggedIn] = useState(false);

    const handleLogin = () => {
        setLoggedIn(true);
    };

    const handleLogout = () => {
        setLoggedIn(false);
    };

    return (
        <div className="App">
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>Banking system</Typography>
                    {loggedIn && (
                        <Button color="inherit" onClick={handleLogout}>Logout</Button>
                    )}
                </Toolbar>
            </AppBar>
            <Container sx={{ marginTop: '2rem', textAlign: 'center' }}>
                <Grid container spacing={3} justifyContent="center">
                    <Grid item xs={12} sm={6}>
                        <Accounts loggedIn={loggedIn} onLogin={handleLogin} />
                    </Grid>
                </Grid>
            </Container>
            {!loggedIn && (
                <Container sx={{ textAlign: 'center', marginTop: '1rem' }}>
                    <Button variant="contained" color="primary" href="http://localhost:8083/login">
                        Login with Google or Facebook
                    </Button>
                </Container>
            )}
        </div>
    );
}

export default App;
