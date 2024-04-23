import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, CircularProgress, Typography } from '@mui/material';

/*const Accounts = () => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [addBalanceAmount, setAddBalanceAmount] = useState(0);

    const sendLoginRequest = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/transaction/accounts/' + accountId)
            .then(response => {
                setAccountData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error retrieving account data:', error);
                setLoading(false);
            });
    };

    const addBalance = () => {
        setLoading(true);
        axios.post('http://localhost:8080/api/transaction/add-balance', {
            id: accountId,
            addBalanceAmount: addBalanceAmount
        })
            .then(response => {
                console.log('Balance added successfully:', response.data);
                sendLoginRequest(); // Refresh account data after balance addition
            })
            .catch(error => {
                console.error('Error adding balance:', error);
                setLoading(false);
            });
    };

    return (
        <>
            <div>
                <label htmlFor="account">Enter your account ID: </label>
                <input type="text" id="account" value={accountId} onChange={(e) => setAccountId(e.target.value)} />
                <button onClick={sendLoginRequest} disabled={loading}>Get Account Data</button>
            </div>

            {loading && <p>Loading...</p>}

            {accountData && (
                <div>
                    <h2>Account Information</h2>
                    {accountData.map(account => (
                        <div key={account.id}>
                            <p>Account ID: {account.id}</p>
                            <p>Owner Profile ID: {accountId}</p>
                            <p>Deposited Balance: {account.balance}</p>
                            <input type="number" value={addBalanceAmount} onChange={(e) => setAddBalanceAmount(e.target.value)} />
                            <button onClick={addBalance} disabled={loading}>Add Balance</button>
                        </div>
                    ))}
                </div>
            )}

            {!loading && !accountData && <p>No account data available.</p>}
        </>
    );
};*/

/*const Accounts = () => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [addBalanceAmount, setAddBalanceAmount] = useState(0);
    const [selectedAccountId, setSelectedAccountId] = useState(null);

    const sendLoginRequest = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/transaction/accounts/' + accountId)
            .then(response => {
                setAccountData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error retrieving account data:', error);
                setLoading(false);
            });
    };

    const handleAddBalance = () => {
        setLoading(true);
        axios.post('http://localhost:8080/api/transaction/add-balance', {
            targetAccountId: selectedAccountId, // Use selectedAccountId instead of accountId
            addBalanceAmount: addBalanceAmount
        })
            .then(response => {
                console.log('Balance added successfully:', response.data);
                setLoading(false);
                sendLoginRequest(); // Refresh account data after balance addition
            })
            .catch(error => {
                console.error('Error adding balance:', error);
                setLoading(false);
            });
    };

    return (
        <>
            <div>
                <label htmlFor="account">Enter your account ID: </label>
                <input type="text" id="account" value={accountId} onChange={(e) => setAccountId(e.target.value)} />
                <button onClick={sendLoginRequest} disabled={loading}>Get Account Data</button>
            </div>

            {loading && <p>Loading...</p>}

            {accountData && (
                <div>
                    <h2>Account Information</h2>
                    {accountData.map(account => (
                        <div key={account.id}>
                            <p>Account ID: {account.id}</p>
                            <p>Owner Profile ID: {accountId}</p>
                            <p>Deposited Balance: {account.balance}</p>
                            <input type="number" value={addBalanceAmount} onChange={(e) => setAddBalanceAmount(e.target.value)} />
                            <button onClick={() => { setSelectedAccountId(account.id); handleAddBalance(); }} disabled={loading}>Add Balance</button>

                        </div>
                    ))}
                </div>
            )}

            {!loading && !accountData && <p>No account data available.</p>}
        </>
    );
};*/
const Accounts = () => {
    const [accountData, setAccountData] = useState(null);
    const [accountId, setAccountId] = useState('');
    const [loading, setLoading] = useState(false);
    const [balanceAmounts, setBalanceAmounts] = useState({});
    const [selectedAccountId, setSelectedAccountId] = useState(null);

    const sendLoginRequest = () => {
        setLoading(true);
        axios.get('http://localhost:8080/api/transaction/accounts/' + accountId)
            .then(response => {
                setAccountData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error retrieving account data:', error);
                setLoading(false);
            });
    };

    const handleAddBalance = (accountId) => {
        setLoading(true);
        axios.post('http://localhost:8080/api/transaction/add-balance', {
            targetAccountId: accountId,
            addBalanceAmount: balanceAmounts[accountId]
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

            {loading && <p>Loading...</p>}

            {accountData && (
                <div>
                    <Typography variant="h6">Account Information</Typography>
                    {accountData.map(account => (
                        <div key={account.id}>
                            <Typography variant="body1">Account ID: {account.id}</Typography>
                            <Typography variant="body1">Owner Profile ID: {accountId}</Typography>
                            <Typography variant="body1">Deposited Balance: {account.balance}</Typography>
                            <TextField
                                type="number"
                                value={balanceAmounts[account.id] || ''}
                                onChange={(e) => handleBalanceChange(account.id, e.target.value)}
                                variant="outlined"
                                fullWidth
                                sx={{ marginTop: '1rem' }}
                            />
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={() => { setSelectedAccountId(account.id); handleAddBalance(account.id); }}
                                disabled={loading || !balanceAmounts[account.id]}
                                sx={{ marginTop: '1rem' }}
                            >
                               Add Balance
                            </Button>
                        </div>
                    ))}
                </div>
            )}

            {!loading && !accountData && <p></p>}
        </div>
    );
};

export default Accounts;
