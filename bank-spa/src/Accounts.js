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
    const [profileId, setProfileId] = useState('');
    const [loading, setLoading] = useState(false);
    const [balanceAmounts, setBalanceAmounts] = useState({});
    const [selectedAccount, setSelectedAccount] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);

    const sendLoginRequest = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/transaction/accounts/' + profileId)
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

    const handleAddBalance = () => {
        setLoading(true);
        axios.post('http://localhost:8080/api/transaction/add-balance', {
            targetAccountId: selectedAccount?.id,
            addBalanceAmount: balanceAmounts[selectedAccount?.id]
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
                        value={profileId}
                        onChange={(e) => setProfileId(e.target.value)}
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
                            <ListItem key={account?.id} button onClick={() => handleAccountClick(account)}>
                                <ListItemText primary={`Account number: ${account?.id}`}/>
                                <ListItemText primary={`Balance: ${account?.balance}`}/>
                            </ListItem>
                        ))}
                    </List>
                    <Dialog open={openDialog} onClose={handleCloseDialog}>
                        <DialogTitle>Account Details</DialogTitle>
                        <DialogContent>
                            <Typography variant="subtitle1">Account number: {selectedAccount?.id}</Typography>
                            <Typography variant="subtitle1">Balance: {selectedAccount?.balance}</Typography>
                            <TextField
                                type="number"
                                label="Add Balance Amount"
                                value={balanceAmounts[selectedAccount?.id] || ''}
                                onChange={(e) => handleBalanceChange(selectedAccount?.id, e.target.value)}
                                fullWidth
                                variant="outlined"
                                sx={{marginBottom: '1rem'}}
                            />
                            <Typography variant="subtitle1">Transaction History:</Typography>
                            <List>
                                {selectedAccount?.sourceTransactions?.map(transaction => (
                                    <ListItem key={transaction.id} sx={{
                                        backgroundColor: 'rgba(255, 0, 0, 0.1)',
                                        display: 'flex',
                                        gap: '10px'
                                    }}>
                                        <ListItemText primary={`To account: ${transaction.targetAccount?.id}`}/>
                                        <ListItemText primary={`Amount: ${transaction.transactionAmount}`}/>
                                        <ListItemText primary={`Description: ${transaction.description}`}/>
                                    </ListItem>
                                ))}
                                {selectedAccount?.targetTransactions?.map(transaction => (
                                    <ListItem key={transaction.id} sx={{
                                        backgroundColor: 'rgba(0, 255, 0, 0.1)',
                                        display: 'flex',
                                        gap: '10px'
                                    }}>
                                        {transaction.sourceAccount
                                            ? <ListItemText primary={`From account: ${transaction.sourceAccount?.id}`}/>
                                            : <ListItemText primary="Funds addedㅤ"/>
                                        }
                                        <ListItemText primary={`Amount: ${transaction.transactionAmount}`}/>
                                        <ListItemText primary={`Description: ${transaction.description}`}/>
                                    </ListItem>
                                ))}
                            </List>
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
