import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, CircularProgress, Typography, List, ListItem, ListItemText, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';

const Accounts = ({ loggedIn, onLogin }) => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedAccount, setSelectedAccount] = useState(null);

    const sendLoginRequest = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/transaction/accounts/' + accountId)
            .then(response => {
                setAccountData(response.data);
                setLoading(false);
                onLogin();
            })
            .catch(error => {
                console.error('Error retrieving account data:', error);
                setLoading(false);
            });
    };

    const handleAccountClick = (account) => {
        setSelectedAccount(account);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    return (
        <div>
            {!loggedIn && (
                <>
                    <TextField
                        id="account"
                        label="Login (ID)"
                        value={accountId}
                        onChange={(e) => setAccountId(e.target.value)}
                        fullWidth
                        variant="outlined"
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={sendLoginRequest}
                        disabled={loading}
                        sx={{ marginTop: '1rem' }}
                    >
                        {loading ? <CircularProgress size={24} /> : 'Login'}
                    </Button>
                </>
            )}
            {loggedIn && accountData && (
                <div>
                    <Typography variant="h6">Accounts</Typography>
                    <List>
                        {accountData.map(account => (
                            <ListItem key={account.id} button onClick={() => handleAccountClick(account)}>
                                <ListItemText primary={`Account number: ${account.id}`} />
                                <ListItemText primary={`Balance: ${account.balance}`} />
                            </ListItem>
                        ))}
                    </List>
                    <Dialog open={openDialog} onClose={handleCloseDialog}>
                        <DialogTitle>Account Details</DialogTitle>
                        <DialogContent>
                            <Typography variant="subtitle1">Account number: {selectedAccount && selectedAccount.id}</Typography>
                            <Typography variant="subtitle1">Balance: {selectedAccount && selectedAccount.balance}</Typography>
                            {/* TODO historie transakci */}
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={handleCloseDialog} color="primary">Close</Button>
                        </DialogActions>
                    </Dialog>
                </div>
            )}

            {!loading && !accountData && !loggedIn && <p></p>}
        </div>
    );
};

export default Accounts;
