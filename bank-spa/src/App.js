import React, { useState } from 'react';

import { AppBar, Toolbar, Typography, Container, Button, Grid } from '@mui/material';
import Accounts from './Accounts';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'; // Import Link from react-router-dom
import { useParams } from 'react-router-dom';


function App() {
    const [loggedIn, setLoggedIn] = useState(false);
    const [userId, setUserId] = useState(null);



    const handleLogin = () => {
        setLoggedIn(true);
    };

    const handleLogout = () => {
        setLoggedIn(false);
        setUserId(null);
    };

    return (
        <Router>
            <div className="App">
                <AppBar position="static">
                    <Toolbar>
                        <Typography variant="h6" sx={{ flexGrow: 1 }}>Banking system</Typography>
                        {loggedIn && (
                            <Button color="inherit" onClick={handleLogout}>Logout</Button>
                        )}
                    </Toolbar>
                </AppBar>
                {!loggedIn && (
                    <Container sx={{ textAlign: 'center', marginTop: '1rem' }}>
                        <Button variant="contained" color="primary" href="http://localhost:8083/login">
                            Login with Google or Facebook
                        </Button>
                    </Container>

                )}

            </div>
            <Container sx={{ marginTop: '2rem', textAlign: 'center' }}>
                <Grid container spacing={3} justifyContent="center">
                    <Grid item xs={12} sm={6}>
            <Routes>
                <Route path="/:ID" element={<Accounts loggedIn={loggedIn} onLogin={handleLogin} />} />
            </Routes>
                    </Grid>
                </Grid>
            </Container>
        </Router>
    );
}

export default App;
