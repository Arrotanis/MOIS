import React, {useEffect, useState} from 'react';
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
import CreateAccountForm from "./CreateAccountForm";
import {useParams} from "react-router-dom";

let date = Date()
const Accounts = ({loggedIn, onLogin}) => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [balanceAmounts, setBalanceAmounts] = useState({});
    const [selectedAccountId, setSelectedAccountId] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);
    const [termDate, setTermDate] = useState(dayjs(date).add(6, 'month'))
    const { ID } = useParams();

    useEffect(() => {
        console.log("ID:", ID); // Print the ID
        setAccountId(ID);// Set the accountId using the ID from the route parameter
        sendLoginRequest(); // Fetch account data when the component mounts or when the ID changes
    }, [ID]); // Add ID to the dependency array
    useEffect(() => {
        // Fetch account data whenever accountId changes
        if (accountId) {
            sendLoginRequest();
        }
    }, [accountId]);
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
            linkedAccountId: selectedAccountId?.id,
            depositedBalance: balanceAmounts[selectedAccountId?.id],
            endDateTime: termDate.format('YYYY-MM-DD'),
            ownerProfileId: null
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

    const handleCreateAccount = () => {
        setLoading(true);
        axios.post('http://localhost:8080/api/transaction/create-account', {
            ownerAccountId: accountId
        })
            .then(response => {
                console.log('Account created successfully:', response.data);
                sendLoginRequest();
            })
            .catch(error => {
                console.error('Error creating account:', error);
            });
    };

    return (
        <div>
            {loggedIn && accountData && (
                <div>
                    <Typography variant="h5">Accounts</Typography>
                    <Typography variant="h6">
                        Total balance: {accountData.reduce((total, account) => total + account.balance, 0)}
                    </Typography>
                    <DialogContent>
                        <Button onClick={handleCreateAccount} variant="contained" color="primary">
                            Open new account
                        </Button>
                    </DialogContent>
                    <List>
                        {accountData.map(account => (
                            <ListItem key={account.id} button onClick={() => handleAccountClick(account)}>
                                <ListItemText primary={`Account number: ${account.id}`}/>
                                <ListItemText primary={`Balance: ${account.balance}`}/>
                            </ListItem>
                        ))}
                    </List>
                    <Dialog open={openDialog} onClose={handleCloseDialog}>
                        <DialogTitle>Account details</DialogTitle>
                        <DialogContent>
                            <div style={{height: '1rem'}}/>
                            <div style={{display: 'flex', alignItems: 'center', marginBottom: '1rem'}}>
                                <TextField
                                    type="number"
                                    label="Amount"
                                    value={balanceAmounts[selectedAccountId?.id] || ''}
                                    onChange={(e) => handleBalanceChange(selectedAccountId?.id, e.target.value)}
                                    variant="outlined"
                                    sx={{flex: 1, marginRight: '1rem'}}
                                />
                                <Button onClick={handleAddBalance} color="primary" variant="contained">Add
                                    balance</Button>
                            </div>
                            <div style={{display: 'flex', alignItems: 'center', marginBottom: '1rem'}}>
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DatePicker
                                        value={termDate}
                                        onChange={(newValue) => setTermDate(newValue)}
                                        minDate={dayjs().add(6, 'month')}
                                    />
                                </LocalizationProvider>
                                <Button onClick={handleAddTerm} color="primary" variant="contained"
                                        sx={{marginLeft: '1rem'}}>Add term deposit</Button>
                            </div>
                            <Typography variant="subtitle1">Transaction History:</Typography>
                            <List>
                                {selectedAccountId?.sourceTransactions.map(transaction => (
                                    <ListItem key={transaction.id} sx={{
                                        backgroundColor: 'rgba(255, 0, 0, 0.1)',
                                        display: 'flex',
                                        gap: '10px'
                                    }}>
                                        <ListItemText primary={`To account: ${transaction.targetAccount}`}/>
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
                                            ? <ListItemText primary={`From account: ${transaction.sourceAccount}`}/>
                                            : <ListItemText primary="Funds addedㅤ"/>
                                        }
                                        <ListItemText primary={`Amount: ${transaction.transactionAmount}`}/>
                                        <ListItemText primary={`Description: ${transaction.description}`}/>
                                    </ListItem>
                                ))}
                            </List>
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
