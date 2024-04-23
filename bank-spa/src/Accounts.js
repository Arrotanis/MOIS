import React, {useState} from 'react';
import axios from 'axios';
import {
    TextField,
    Button,
    CircularProgress,
    Typography,
    List,
    ListItem,
    ListItemText,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions
} from '@mui/material';

const Accounts = ({loggedIn, onLogin}) => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [balanceAmounts, setBalanceAmounts] = useState({});
    const [selectedAccountId, setSelectedAccountId] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);

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
        setSelectedAccountId(account);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleAddBalance = () => {
        setLoading(true);
        axios.post('http://localhost:8080/api/transaction/add-balance', {
            targetAccountId: selectedAccountId?.id,
            addBalanceAmount: balanceAmounts[selectedAccountId?.id]
        })
            .then(response => {
                console.log('Balance added successfully:', response.data);
                setLoading(false);
                sendLoginRequest();
            })
            .catch(error => {
                console.error('Error adding balance:', error);
                setLoading(false);
            });
    };

    const handleBalanceChange = (accountId, value) => {
        setBalanceAmounts(prevState => ({
            ...prevState,
            [accountId]: value
        }));
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
                        sx={{marginTop: '1rem'}}
                    >
                        {loading ? <CircularProgress size={24}/> : 'Login'}
                    </Button>
                </>
            )}
            {loggedIn && accountData && (
                <div>
                    <Typography variant="h6">Accounts</Typography>
                    <List>
                        {accountData.map(account => (
                            <ListItem key={account.id} button onClick={() => handleAccountClick(account)}>
                                <ListItemText primary={`Account number: ${account.id}`}/>
                                <ListItemText primary={`Balance: ${account.balance}`}/>
                            </ListItem>
                        ))}
                    </List>
                    <Dialog open={openDialog} onClose={handleCloseDialog}>
                        <DialogTitle>Account Details</DialogTitle>
                        <DialogContent>
                            <Typography variant="subtitle1">Account
                                number: {selectedAccountId && selectedAccountId?.id}</Typography>
                            <Typography
                                variant="subtitle1">Balance: {selectedAccountId && selectedAccountId.balance}</Typography>
                            <TextField
                                type="number"
                                label="Add Balance Amount"
                                value={balanceAmounts[selectedAccountId?.id] || ''}
                                onChange={(e) => handleBalanceChange(selectedAccountId?.id, e.target.value)}
                                fullWidth
                                variant="outlined"
                                sx={{marginTop: '1rem'}}
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={handleAddBalance} color="primary">Add Balance</Button>
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
