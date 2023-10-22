import React from 'react'
import { Typography, Button, Table, Tag } from 'antd'
import { CheckOutlined, DeleteOutlined } from '@ant-design/icons'
const { Title } = Typography
function LoansTable({ loans  }) {
    const columns = [
        {
            title: 'Loan ID',
            dataIndex: 'loanId',
            key: 'loanId',
        },
        {
            title: 'Loan Value',
            dataIndex: 'loanValue',
            key: 'loanValue',
        },
        {
            title: 'Tax Loan',
            dataIndex: 'taxLoan',
            key: 'taxLoan',
        },
        {
            title: 'Total Loan Value',
            dataIndex: 'totalLoanValue',
            key: 'totalLoanValue',
        },
        {
            title: 'Number of Installments',
            dataIndex: 'numberInstallments',
            key: 'numberInstallments',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
        },
    ];
    const dataSource = loans.map(loan => {
        return {
            loanId: loan.loanId,
            loanValue: loan.loanValue,
            taxLoan: loan.taxLoan,
            totalLoanValue: loan.totalLoanValue,
            numberInstallments: loan.numberInstallments,
            status: loan.status,
        }
    });
    return (
        <>
            <Title level={3}>Loans Table</Title>
            <Table
                dataSource={dataSource}
                columns={columns}
            />
        </>
    )
}
export default LoansTable;
