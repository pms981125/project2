/*
	비동기함수를 이용해서 결과를 처리하는 방식은 크게 다음과 같다.
	1. 비동기함수에선 순수하게 비동기통신만 처리 하고 호출한 곳에서
	   then().catch()등을 이용해서 처리하는 방법
	2. 비동기함수를 호출할 때 나중에 처리해야 하는 내용등을 별도의 
	   함수로 구성해서 파라미터로 전송하는 방식
*/

async function getList({accountId, page, size, goLast}) {
	const result = await axios.get(`/accountHistory/list/${accountId}`, {params: {page, size, goLast}})
	
	if(goLast){
	    const total = result.data.total
	    const lastPage = parseInt(Math.ceil(total/size))
	    return getList({accountId:accountId, page:lastPage, size:size})
	}    	
	
	return result.data
}

async function addTransfer(transferObj) {
	const response = await axios.post(`/accountHistory/`, transferObj)
	return response.data
}




