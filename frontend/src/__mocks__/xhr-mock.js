const xhrMockClass = () => ({
    open            : jest.fn().mockReturnValue("mock"),
    send            : jest.fn().mockReturnValue("mock"),
    setRequestHeader: jest.fn(),
    responseText: "mock"
});

window.XMLHttpRequest = jest.fn().mockImplementation(xhrMockClass)