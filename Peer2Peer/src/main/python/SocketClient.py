import socket
import sys
import json
# specify where the generated protofiles are
sys.path.append('../../../build/generated/source/proto/main/python')

from operation_pb2 import Operation
from response_pb2 import Response
from google.protobuf.internal.encoder import _VarintBytes
from google.protobuf.internal.decoder import _DecodeVarint32


class SocketClient(object):
    def __init__(self, host):
        self.host = host
        self.port = port

    def connect(self):

        try:
            # connect to the server
            serverSock = socket.socket()
            serverSock.connect((self.host, self.port))
        except socket.error:
            print('Failed to create socket')
            sys.exit()

        question = self.generateQuestionObject(data)

        # receive message
        received = serverSock.recv(1024)

        # convert message to PB object
        msgLen, newPos = _DecodeVarint32(received, 0)
        response.ParseFromString(received[newPos:newPos + msgLen])

        print("Result is: ", response.resultString)

        serverSock.close()  # close the connection

    def generateOperationObject(self, data):
        operation = Operation()
        try:
            header = data["header"]
            payload = data["payload"]
            operation.val1 = payload["num1"]
            operation.val2 = payload["num2"]
            operation.base = header["base"]
            operation.operationType = self.OPERATION_TYPES[header["operation"].lower()]
            operation.responseType = self.RESPONSE_TYPES[header["response"].lower()]
            return operation
        except KeyError as err:
            raise KeyError  # handle exception


if __name__ == "__main__":
    if len(sys.argv) != 4:
            raise ValueError("Expected arguments: <host(String)> <port(int)>")
    print(sys.argv)
    _, host, port = sys.argv  # host, port
    socketClient = SocketClient(host, int(port))
    socketClient.connect()

