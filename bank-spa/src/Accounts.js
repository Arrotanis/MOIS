import React, {useState} from 'react';
import axios from 'axios';
import {DatePicker} from "@mui/x-date-pickers";
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from 'dayjs';
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

let date = Date()
const Accounts = ({loggedIn, onLogin}) => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [balanceAmounts, setBalanceAmounts] = useState({});
    const [selectedAccountId, setSelectedAccountId] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);
    const [termDate, setTermDate] = React.useState(dayjs(date))

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

    const handleAddTerm = () => {
        setLoading(true);
        console.log('Selected date:', termDate.format('YYYY-MM-DD'));
        axios.post('http://localhost:8080/api/deposit/create', {
            targetAccountId: selectedAccountId?.id,
            date: termDate.format('YYYY-MM-DD')
        })
            .then(response => {
                console.log('Deposit created successfully:', response.data);
            })
            .catch(error => {
                console.error('Error creating deposit:', error);
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
                            <Typography variant="subtitle1">Account number: {selectedAccountId?.id}</Typography>
                            <Typography variant="subtitle1">Balance: {selectedAccountId?.balance}</Typography>
                            <TextField
                                type="number"
                                label="Amount"
                                value={balanceAmounts[selectedAccountId?.id] || ''}
                                onChange={(e) => handleBalanceChange(selectedAccountId?.id, e.target.value)}
                                fullWidth
                                variant="outlined"
                                sx={{marginBottom: '1rem'}}
                            />
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <DatePicker
                                    value={termDate}
                                    onChange={(newValue) => setTermDate(newValue)}
                                />
                            </LocalizationProvider>
                            <Typography variant="subtitle1">Transaction History:</Typography>
                            <List>
                                {selectedAccountId?.sourceTransactions.map(transaction => (
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
                                {selectedAccountId?.targetTransactions.map(transaction => (
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
                            <Button onClick={handleAddTerm} color="primary">Add term deposit</Button>
                            <Button onClick={handleAddBalance} color="primary">Add balance</Button>
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
