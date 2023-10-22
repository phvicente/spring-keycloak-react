import axios from "axios";

export const loanApi = {
    createLoan,
    getLoanById,
    getLoansByCustomerId
};


function createLoan(createLoanRequest, access_token) {
    return instance.post('', createLoanRequest, {
        headers: { 'Content-type': 'application/json', 'Authorization': `Bearer ${access_token}` }
    });
}

function getLoanById(id, access_token) {
    return instance.get(`/${id}`, {
        headers: { 'Authorization': `Bearer ${access_token}` }
    });
}

function getLoansByCustomerId(access_token) {
    return instance.get('/customer', {
        headers: { 'Authorization': `Bearer ${access_token}` }
    });
}
const instance = axios.create({
    baseURL: 'http://localhost:8081/loan'
});
