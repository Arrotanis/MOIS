import React, { useState } from 'react';
import axios from 'axios';

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
    const [balanceAmounts, setBalanceAmounts] = useState({}); // State to store balance amounts for each account
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
            targetAccountId: accountId, // Use accountId instead of selectedAccountId
            addBalanceAmount: balanceAmounts[accountId] // Get balance amount from state for the specific account
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

    const handleBalanceChange = (accountId, value) => {
        // Update the balance amount for the specific account
        setBalanceAmounts(prevState => ({
            ...prevState,
            [accountId]: value
        }));
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
                            <input type="number" value={balanceAmounts[account.id] || ''} onChange={(e) => handleBalanceChange(account.id, e.target.value)} />
                            {/* Enable the button only when balanceAmounts[account.id] is not null */}
                            <button onClick={() => { setSelectedAccountId(account.id); handleAddBalance(account.id); }} disabled={loading || !balanceAmounts[account.id]}>Add Balance</button>
                        </div>
                    ))}
                </div>
            )}

            {!loading && !accountData && <p>No account data available.</p>}
        </>
    );
};


export default Accounts;
