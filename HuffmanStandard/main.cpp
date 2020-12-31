#include <bits/stdc++.h>
using namespace std;
string data;
vector<vector<int>> adjList;
map<string, char> binaryToChar;
map<int, char> idMap;
map<char, string> charToBinary;
string decoded, encoded;
struct Node {
    char c;
    double probability;
    int id;
    Node(char c, double probability, int id) {
        this->probability = probability;
        this->c = c;
        this->id = id;
    }

    bool operator<(const Node &m) const {
        return this->probability < m.probability;
    }
};
multiset<Node> prob;
int id;
void getProbability(string data) {
    prob.clear();
    map<char, int> numberOfChar;
    for (char i:data) {
        numberOfChar[i]++;
    }
    int All = data.size(); /// number of all char
    for (auto i:numberOfChar) {
        idMap[id] = i.first;
        prob.insert(Node(i.first, (i.second * 1.0) / All, id++));
    }
}

void buildBinaryTree() {
    adjList.resize(id);
    while (prob.size() > 2) {
        auto last = *prob.begin();
        prob.erase(prob.begin());
        auto prevLast = *prob.begin();
        prob.erase(prob.begin());
        double newProbability = last.probability + prevLast.probability;
        if(id==adjList.size()) { /// if id out range in  adjList
            adjList.emplace_back();
        }
        adjList[id].push_back(last.id);
        adjList[id].push_back(prevLast.id);
        prob.emplace(Node{last.c, newProbability, id});
        id++;
    }
};
void dfs(int u,string binary) {
    char bit = '0';
    if (idMap.find(u) != idMap.end()) {
        binaryToChar[binary] = idMap[u];
        charToBinary[idMap[u]] = binary;
    }
    for (auto v:adjList[u]) {
        dfs(v, binary + bit);
        bit++;
    }
};
void compress() {
    encoded.clear();
    for (char c:data) {
        encoded += charToBinary[c];
    }
};
void decompress() {
    string sub = "";
    decoded.clear();
    for (char c:encoded) {
        sub += c;
        if (binaryToChar.find(sub) != binaryToChar.end()) {
            decoded.push_back(binaryToChar[sub]);
            sub.clear();
        }
    }
}
int main() {
    /// test
    cin >> data;
    getProbability(data);
    buildBinaryTree();
    dfs(prob.begin()->id, "0");
    prob.erase(prob.begin());
    if (!prob.empty()) {
        dfs(prob.begin()->id, "1");
    }
    compress();
    decompress();
    for(auto i:charToBinary){
        cout<<i.first<<' '<<i.second<<endl;
    }
    cout << encoded << endl;
    cout << decoded << endl;
    return 0;
    /*
     * 101
     * 1011
     */
}
