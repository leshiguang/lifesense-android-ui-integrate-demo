path=$1
dirs=${path%/*}
mkdir -p $dirs
curl -o $1 $2